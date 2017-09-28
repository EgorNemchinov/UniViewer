# UniViewer
Console-application able to view files of certain types.
Even though the only supported type is BMP, one of the main advantages of this MVC architecture is that new modules (image formats, user interfaces, etc.) are easy to add.

### Isolation
Each part of the project know as little as possible about the structure of another one. This is accomplished via creating interfaces for each part of the application:
 - Controller interface: ```openFile(path)``` .
 - View interface: ```drawImage(image)```
 - Parser interface: ```parseFile(bytes)```
 - Image format template contains ```width``` and ```height```
Modules communicate (pass information between each other) using a Observer.

### How does it work
Here's diagram, I think that's quite enough to get basic understanding:
![Diagram](https://user-images.githubusercontent.com/22173703/30978035-0a250de0-a482-11e7-811f-dcca49101627.png)

### Algorithm for adding new file format
 - Create Parser for the format
 - Create ImageInfo for that format
 - Change ParserFactory to return certain Parser for this extension

### Try it out
To run all the tests, execute [src/test/kotlin/BmpTest.kt](https://github.com/ImmortalTurtle/UniViewer/blob/master/src/test/kotlin/BmpTest.kt)
