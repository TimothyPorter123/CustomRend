# CustomRend
CustomRend was concieved as a person challenge in which I am attempting to recreate the traditional rendering pipeline in java for use as a 3d modeling program without the use of external rendering libraries.

## What's done so far?
To date all the required mathematics infatructure for the rendering has been completed, and a basic rendering pipeline established which takes in custom scripted shaders and applies them to models in a scene.

<img src="res/SampleFootage.gif" alt="sample-gif" width="400"/>

## What's to come?
The next steps are improving user control by allowing for model manipulation directly within the program, along with this a basic GUI must be implemeneted to guide the user experience.
Additionally, which the rendering pipeline is completely setup it lacks the ability to interact with custom real-time lighting. To do this a second rendering pass will be required.

## What am I looking at?
Below I've included a basic summary of how the class structure of the project is organized, and what each substructure is responsible for.

### Model
The model consists of all classes which form a basic for representing 3d shapes in space. It does not include the visualization tools (shaders, projection maps etc.) which are used in the rendering pipeline, just the raw data needed to reprsent objects
* Mathematics:
  * For the purposes of this project, Point and Matrix classes were not used and instead custom Vector and TransformationMatrix classes were made to better suit the needs of the project.
* Objects:
  * This folder contains abstract classes designed to represent any 3d object, as well as some final classes which represent basic shapes for the purposes of testing
  * This folder also contains the camera classes, which have been labeled objects for the purpose of placing them in scenes
* Scene3D: A 3D scene containing one or more 3d object models

### View
The view consists of all classes responsible for turning model data into visuals through the 3d rendering pipeline.
* Shaders: These are scripts which outline how any given object is to be rendered, first through the vert() pass which occurs once per vertex, then the frag() or fragmentation pass which occurs once per pixel to produce the final output
* UserModelWindow: A window in the program through which a Scene3D can be viewed
* PreImage: A more bare-bones form of a BufferedImage used for internal processing before output for efficiency
### Controller
This contains all user input modes used to manipulate the settings of the program or the scene, currently it supports moving the viewing camera and taking keystroke inputs
