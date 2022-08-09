use maze_generator::{FnValidator, MazeGenerator};

mod maze_generator;
fn main() -> Result<(), image::ImageError> {
    let width = 512;
    let height = 512;
    let center_x = width as f32 / 2f32;
    let center_y = height as f32 / 2f32;

    let maze_generator = MazeGenerator::new(Vec::new(), width, height);
    let image = maze_generator.run(0, 0);
    image.save("./maze.png")?;

    let circle_maze_generator = MazeGenerator::new(
        vec![Box::new(FnValidator::from(move |x, y| {
            let delta_x = center_x - x as f32;
            let delta_y = center_y - y as f32;
            let dist = (delta_x * delta_x + delta_y * delta_y).sqrt();
            dist < center_x || x % 2 == 0 || y % 2 == 0
        }))],
        width,
        height,
    );
    let circle_image = circle_maze_generator.run(center_x as i32, center_y as i32);
    circle_image.save("./circle_maze.png")?;

    Ok(())
}
