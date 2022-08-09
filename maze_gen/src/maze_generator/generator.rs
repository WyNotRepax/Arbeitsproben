use std::collections::HashSet;

use image::{ImageBuffer, Luma};
use rand::seq::SliceRandom;

type MazeImage = ImageBuffer<Luma<u8>, Vec<u8>>;

use super::{CellValidator, WithinSizeValidator};

pub struct MazeGenerator {
    cell_validators: Vec<Box<dyn CellValidator>>,
    img_width: u32,
    img_height: u32,
}

impl MazeGenerator {
    pub fn new(
        mut cell_validators: Vec<Box<dyn CellValidator>>,
        maze_width: u32,
        maze_height: u32,
    ) -> Self {
        cell_validators.push(Box::new(WithinSizeValidator::new(maze_width, maze_height)));
        Self {
            cell_validators,
            img_height: 2 * maze_height + 1,
            img_width: 2 * maze_width + 1,
        }
    }

    pub fn run(&self, start_x: i32, start_y: i32) -> MazeImage {
        let mut image = self.generate_image_with_walls();
        // return image;

        let mut discovered = HashSet::new();
        let mut last = Vec::new();
        let mut cell_x = start_x;
        let mut cell_y = start_y;

        'outer: loop {
            discovered.insert((cell_x, cell_y));
            'inner: for (x_offset, y_offset) in get_offsets(true) {
                let new_cell_x = cell_x + x_offset;
                let new_cell_y = cell_y + y_offset;
                if self
                    .cell_validators
                    .iter()
                    .any(|validator| !validator.is_cell_valid(new_cell_x, new_cell_y))
                    || discovered.contains(&(new_cell_x, new_cell_y))
                {
                    continue 'inner;
                }

                self.remove_wall(&mut image, cell_x, cell_y, new_cell_x, new_cell_y);

                last.push((cell_x, cell_y));
                cell_x = new_cell_x;
                cell_y = new_cell_y;
                continue 'outer;
            }
            match last.pop() {
                None => break 'outer,
                Some((last_cell_x, last_cell_y)) => {
                    cell_x = last_cell_x;
                    cell_y = last_cell_y;
                }
            }
        }

        image
    }

    #[allow(unused)]
    pub fn run_recursive(&self, start_x: i32, start_y: i32) -> MazeImage {
        let mut image = self.generate_image_with_walls();
        self.expand_recursively(start_x, start_y, &mut image, &mut HashSet::new());
        image
    }

    fn expand_recursively(
        &self,
        cell_x: i32,
        cell_y: i32,
        img: &mut ImageBuffer<Luma<u8>, Vec<u8>>,
        discovered: &mut HashSet<(i32, i32)>,
    ) {
        discovered.insert((cell_x, cell_y));

        for (x_offset, y_offset) in get_offsets(true) {
            let new_cell_x = (cell_x as i32) + x_offset;
            let new_cell_y = (cell_y as i32) + y_offset;

            if self
                .cell_validators
                .iter()
                .any(|validator| !validator.is_cell_valid(new_cell_x, new_cell_y))
                || discovered.contains(&(new_cell_x, new_cell_y))
            {
                continue;
            }

            self.remove_wall(img, cell_x, cell_y, new_cell_x, new_cell_y);
            self.expand_recursively(new_cell_x, new_cell_y, img, discovered);
        }
    }

    fn generate_image_with_walls(&self) -> MazeImage {
        ImageBuffer::from_fn(self.img_width, self.img_height, |x, y| {
            if x % 2 == 0 || y % 2 == 0 {
                Luma([0u8])
            } else {
                Luma([255u8])
            }
        })
    }

    fn remove_wall(
        &self,
        img: &mut MazeImage,
        cell1_x: i32,
        cell1_y: i32,
        cell2_x: i32,
        cell2_y: i32,
    ) {
        let wall_x = cell1_x + cell2_x + 1;
        let wall_y = cell1_y + cell2_y + 1;
        if wall_x == 0 {
            println!("{} {} {} {}", cell1_x, cell1_y, cell2_x, cell2_y)
        }

        if wall_x >= 0
            && wall_x < self.img_width as i32
            && wall_y >= 0
            && wall_y < self.img_height as i32
        {
            img.put_pixel(wall_x as u32, wall_y as u32, Luma([255u8]));
        } else {
            println!(
                "Warning: Tried to remove wall at invalid location ({},{})",
                wall_x, wall_y
            );
        }
    }
}

fn get_offsets(randomize: bool) -> [(i32, i32); 4] {
    let mut offsets = [(1, 0), (-1, 0), (0, 1), (0, -1)];
    if randomize {
        offsets.shuffle(&mut rand::thread_rng());
    }
    offsets
}
