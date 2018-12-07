// Introduction to ImageJ2 Scripting
// =================================

// (authored by Curtis Rueden and Jan Eglinger for I2K 2018)

/*
 * This script will introduce some basics of
 * the scripting functionality in ImageJ2
 * 
 * 1. How to get started with script parameters
 * 2. ImageJ API: SciJava Services and Ops
 * 3. Calling scripts from other scripts
 * 4. Using ImgLib2 ROIs
 * 5. How to mix and match IJ1 and IJ2 API
 */

//*********************************************\\
// 1. How to get started with script parameters

// What are script parameters?
// They look like this:

#@ Dataset image

/*
 * The "hash at" (#@) is used to identify a script parameter.
 * The "Dataset" is the type of the input. In this case, we want an image.
 * There are several types associated with images, but when in doubt, just use Dataset. :-)
 * The "image" is the name of the variable.
 * The input value will be assigned differently depending on the context.
 * In this case, we have a single image input, so the active ImageJ image will be used.
 * I.e.: the user will not need to explicitly select anything from any dialog box.
 */

// Let's do something with our image. First print its name:
println(image.getName())

return // REMOVE THIS LINE TO CONTINUE BEYOND THIS POINT

// UNCOMMENT THE FOLLOWING LINES TO CONTINUE
//#@ String (label="Your name") name

/*
 * In the above line, we're asking for a "name" text input.
 * If parameter values are not provided otherwise
 * (e.g. when calling from another script or macro),
 * a dialog box will pop up letting the user enter or choose a value.
 * We can customize how this input dialog looks using parameter attributes
 * such as the "label" in the above example.
 * 
 * Various basic types are supported, some of the common types are illustrated below.
 * For a more complete listing of types, see:
 * https://imagej.net/Script_Parameters#Parameter_types
 */

// An informative message
//#@ String (visibility=MESSAGE, value="Please enter some parameter values", persist=false, required=false) msg

// We can provide a set of predefined choices
//#@ String (label="Which measurement?", choices={mean,median,min,max}) measurement

// A File parameter can have two styles: "open" (the default) or "save"
//#@ File (style=save, label="Save image to") destinationFile

// A slider to choose a numeric input value
//#@ Double (style=slider, min=0.5, max=10, stepSize=0.5, columns=3) someValue

// [EXERCISE] Add an Integer parameter in the range 0-3 with style "scroll bar"
//#@ Integer ...

/*
 * So far, we only asked for INPUT parameters,
 * but we can also define OUTPUTs.
 * 
 * Output parameters will be processed by the framework after executing the script.
 * Most known output types (e.g. numbers, text or boolean values)
 * will be shown in a results table.
 * 
 * NB: the type specification is optional for outputs, see below
 */

//#@output Boolean success

success = true

return // REMOVE THIS LINE TO CONTINUE BEYOND THIS POINT

/*
 * NB: Note that all these script parameters also work in .ijm macros!
 * No need to use Dialog.create, Dialog.addNumber, Dialog.show etc. anymore :-)
 */


//*********************************************\\
// 2. ImageJ API: SciJava Services and Ops

/*
 * A large part of the ImageJ functionality is provided by services.
 * 
 * Let us introduce some of the most important services here.
 * We can access them easily using script parameters:
 */

// LogService can be used to log e.g. infos, warnings and errors
#@ LogService log

log.info("This is an info message.")
log.warn("This is a warning.")
log.error("This is an error.")


// IOService can be used to open and save images or other data
#@ IOService io

baboon = io.open("https://imagej.net/images/baboon.gif")


// UIService can be used to display an image or other data
#@ UIService ui

ui.show(baboon)

return // REMOVE THIS LINE TO CONTINUE BEYOND THIS POINT

/*
 * Note that in addition to IOService, there's also DatasetIOService,
 * which has more control over how images are opened.
 */

// Finally, there's OpService, giving access to the powerful ImageJ Ops

#@ OpService ops

// Let's also define another output here:

//#@output value

// Now we run the "stats.mean" op.
// (Note that it returns an ImgLib2 Type, so we need to call getRealDouble() to get its value.)
value = ops.run("stats.mean", image).getRealDouble()


// [EXERCISE] Change the above ops call to compute any of stats.mean, stats.median, stats.min, stats.max
// according the user choice of the "measurement" parameter

/*
 * For more services, see https://imagej.net/SciJava_Common#Services
 */



//*********************************************\\
// 3. Calling scripts from other scripts

/*
 * You can make a script showing up as a menu command
 * by saving it into a subfolder of the ./scripts/ directory,
 * while making sure its name contains an underscore (_)
 *
 * The script execution can then be recorded,
 * and the script can be called from another script.
 * 
 * Let's test this in practice.
 */

// [EXERCISE] Save this script into
//               ./scripts/I2K/My_first_script.groovy
// inside your Fiji.app folder, then restart Fiji

// [EXERCISE] Now start the macro recorder (Plugins > Macros > Record...)
// and run our script (I2K > My first script)

// [EXERCISE] Click "Create" in the macro recorder
// and see how you can run the script with prefilled parameter values

/*
 * NB1: To open a script from the menu in the script editor,
 * hold down SHIFT and then click on the menu entry.
 */

/*
 * NB2: Any script having a file or image input can now be run in batch with a single click:
 * try out the new "Batch" button of the script editor, or "Batch" in the search bar result.
 */

return // REMOVE THIS LINE TO CONTINUE BEYOND THIS POINT



//*********************************************\\
// 4. Using ImgLib2 ROIs


/* 
 * Let's explore the new ImgLib2 ROIs a bit.
 * 
 * We'll get an (ImageJ1) ROI from a RoiManager,
 * convert it into an IJ2 ROI: a RealMask,
 * and perform an operation on each pixel inside the ROI
 * 
 * The following code is adapted from a course for the 2018 dais Learnathon:
 * https://github.com/maarzt/imagej-legacy-course
 */

// We'll need some new inputs:
//#@ RoiManager rm
//#@ ConvertService convertService

// Get the first ROI in the ROI Manager
roi = rm.getRoi(0)

// Convert it to RealMask (i.e. an ImgLib2 ROI)

import net.imglib2.roi.RealMask
convertedRoi = convertService.convert(roi, RealMask.class)

// Create a new polygon2d 

import net.imglib2.roi.geom.GeomMasks
newRoi = GeomMasks.polygon2D( [10, 20, 110, 110, 100, 10] as double[], [10, 10, 100, 110, 110, 20] as double[])

// Combine the two ROIs

combinedRoi = convertedRoi.or(newRoi)

// Iterate over all elements (pixel) inside the ROI an set them to a constant value
import net.imglib2.roi.Masks
import net.imglib2.roi.Regions
import net.imglib2.view.Views

iterableROI = Regions.iterable(
							  Views.interval(
											Views.raster(
														Masks.toRealRandomAccessible(combinedRoi)
														),
							 				image
							 				)
							  )
iterableImageOnROI = Regions.sample( iterableROI, image )

iterableImageOnROI.forEach({t -> t.setReal(50)})

// [EXERCISE] Change the above code to create an intersection (instead of a union) or the two ROIs

// Let's save the image now
io.save(image, destinationFile.getPath())

return // REMOVE THIS LINE TO CONTINUE BEYOND THIS POINT



//*********************************************\\
// 5. How to mix and match IJ1 and IJ2 API

/*
 * In the ROI section above, we've already seen how to mix
 * IJ1 and IJ2 structures (Roi and RealMask).
 * 
 * In many cases, we can rely on the framework to do the conversion autmatically.
 * 
 * For other cases, we can use the ConvertService to convert from one type to another.
 */

// If we need to have the active image both as (IJ2) Dataset and (IJ1) ImagePlus,
// we can just use two input parameters (the Dataset above and a new ImagePlus here):

#@ ImagePlus imp

// Run an ImageJ1 plugin, e.g. Invert...
import ij.IJ
IJ.run(imp, "Invert", "")

// If we really need to convert between the two,
// (e.g. because you create a new image and need to process it)
// we have several options to do so:

// a) ConvertService.convert()
// b) LegacyService.getImageMap.register...
// c) ImageJFunctions.wrap()

// Create a new image using Ops

sinusoidImage = ops.run("create.img", [100, 100])

// Fill image with some data

ops.run("image.equation", sinusoidImage, "63 * (Math.cos(0.3*p[0]) + Math.sin(0.3*p[1])) + 127")

// Create a Dataset from the result
#@ DatasetService datasetService
sinusoidDataset = datasetService.create(sinusoidImage)

// UNCOMMENT THE LINE BELOW TO SHOW THE IMAGE
//ui.show(sinusoidDataset)

// Convert the image

// 1. Using ConvertService
import ij.ImagePlus
sinusoidImp = convertService.convert(sinusoidDataset, ImagePlus.class)

// 2. Using LegacyService

// 3. Using ImageJFunctions

/*
 * For further information on mixing and matching IJ1 and IJ2, see:
 * https://imagej.net/ImageJ1-ImageJ2_cheat_sheet
 */

// Run "Find Maxima...", an ImageJ1 plugin, to count the maxima

IJ.run(sinusoidImp, "Find Maxima...", "noise=10 output=Count")
