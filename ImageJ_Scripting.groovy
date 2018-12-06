//*********************************************\\
// 1. How to get started with script parameters

// What are script parameters?
// They look like this:

#@ Dataset image

/* 
The "hash bang" (#@) is used to identify a script parameter.
The "Dataset" is the type of the input. In this case, we want an image.
There are several types associated with images, but when in doubt, just use Dataset. :-)
The "image" is the name of the variable.
The input value will be assigned differently depending on the context.
In this case, we have a single image input, so the active ImageJ image will be injected.
I.e.: the user will not need to explicitly select anything from any dialog box.
*/

// Do something with our image.
println(image.getName())

return // REMOVE ME TO CONTINUE

// UNCOMMENT THE FOLLOWING LINE TO CONTINUE
//#@ String (visibility=message, value="Choose stuff")

/*
Various basic types are supported by popping up a dialog box letting the user choose.
The above illustrate some of the common types.
For a more complete listing of types, see:
https://imagej.net/Script_Parameters#Parameter_types
*/



//#@ String 



//#@ Double (



//#@ String (style=radioButtonsHorizontal, choices={yes, no, maybe}) agreement


// [ACTION] Add another Double parameter with slider bar


//#@ IOService datasetIO



//#@ File (style=save, label="Save image to") destinationFile



//#@output success


success = true

return // TODO: Remove this!

//*********************************************\\
// 2. How to get started with the ImageJ API

// ImageJ API intro: services, ops
// -LogService
// -OpService
// -IOService (and mention DatasetIOService)
// -Link to place where you can read about many others!

// 1. Open the image
// Thknk about whether we want to show opening stuff here.
// Maybe fine, and we just delete that parameter ("COMMENT/DELETE ME TO CONTINUE")
// There is also the DatasetIOService, which has more control over how images are opened.

//*********************************************\\
// 3. How to save a script into the scripts/ folder so it's in the menus
// and can be recorded -- and calling one script from another script
// -mention to use scripts/ dir with underscore in the name somewhere
// -mention recordability
// -mention batch running
// -can do this with any script

//*********************************************\\
// 4. ROIs using ImgLib2

// We covered this in the 2018 learnathon, but together with IJ1<->IJ2.
// Maybe we should also mix them a bit here? Whatever is easier.
// https://github.com/maarzt/imagej-legacy-course/blob/solution-operation-on-roi/src/main/java/de/mpicbg/learnathon/course/legacy/exercise3_rois/OperationOnRoi.java

// For displaying ROIs:
// https://github.com/maarzt/imagej-legacy-course/blob/solution-display-roi/src/main/java/de/mpicbg/learnathon/course/legacy/exercise3_rois/DisplayRoi.java
// However, let's try not to use ROITree.
// Instead, you can use ROIService.add(roi, dataset)
// And then: ui.show(dataset) // <-- automagical conversion to ImagePlus

//*********************************************\\
// 5. How to mix and match ImageJ1

// See link above -- maarzt/imagej-legacy-course
// Mostly really awesome. A few places that can be trimmed / improved
