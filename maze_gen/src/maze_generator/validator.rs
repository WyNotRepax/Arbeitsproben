pub trait CellValidator {
    fn is_cell_valid(&self, cell_x: i32, cell_y: i32) -> bool;
}

pub struct WithinSizeValidator {
    maze_width: u32,
    maze_height: u32,
}

impl CellValidator for WithinSizeValidator {
    fn is_cell_valid(&self, cell_x: i32, cell_y: i32) -> bool {
        cell_x >= 0
            && cell_x < self.maze_width as i32
            && cell_y >= 0
            && cell_y < self.maze_height as i32
    }
}

impl WithinSizeValidator {
    pub fn new(maze_width: u32, maze_height: u32) -> Self {
        Self {
            maze_width,
            maze_height,
        }
    }
}

pub struct FnValidator {
    func: Box<dyn Fn(i32, i32) -> bool>,
}

impl<T: Fn(i32, i32) -> bool + 'static> From<T> for FnValidator {
    fn from(func: T) -> Self {
        Self {
            func: Box::new(func),
        }
    }
}

impl CellValidator for FnValidator {
    fn is_cell_valid(&self, cell_x: i32, cell_y: i32) -> bool {
        (self.func)(cell_x, cell_y)
    }
}
