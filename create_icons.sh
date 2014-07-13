#!/bin/bash
cd workspace
if [ ! -d tmp ]
then
  mkdir tmp
fi
declare -a flavours=(pork beef prawn vegetable)
declare -a widgets=(dumpling_waiting dumpling_here star_empty star_full)
declare -a widgetSizes=(164 128 84 64 42)
declare -a iconSizes=(192 144 96 72 48)
declare -a aboutLogoSizes=(1024 768 512 384 256)
declare -a overlays=(160 120 80 60 40)
declare -a resNames=(xxxhdpi xxhdpi xhdpi hdpi mdpi)

# Dumpling icons. Cycle through the resource sizes, and for each size...
for sizeIndex in 0 1 2 3 4
do
  resName=${resNames[sizeIndex]}
  outputDir=../Dumplings/src/main/res/drawable-${resName}

  iconSize=${iconSizes[sizeIndex]}
  overlay=${overlays[sizeIndex]}
  # ...render a generic dumpling...
  convert -background none simple_dumpling.svg -resize ${iconSize}x${iconSize} tmp/simple_dumpling_${resName}.png
  cp tmp/simple_dumpling_${resName}.png ${outputDir}/simple_dumpling.png
  for flavour in ${flavours[@]}
  do
    # ...and dumpling flavours for each flavour. 
    convert -background none ${flavour}_icon.svg -resize ${overlay}x${overlay} tmp/${flavour}_icon_${resName}.png
    composite -gravity center tmp/${flavour}_icon_${resName}.png tmp/simple_dumpling_${resName}.png tmp/${flavour}_dumpling_${resName}.png
    cp tmp/${flavour}_dumpling_${resName}.png ${outputDir}/${flavour}_dumpling.png
    rm tmp/${flavour}_icon_${resName}.png
  done

  # Now, on to the widgets.
  widgetSize=${widgetSizes[sizeIndex]}
  for widget in ${widgets[@]}
  do
    convert -background none ${widget}.svg -resize ${widgetSize}x${widgetSize} tmp/${widget}_${resName}.png
    cp tmp/${widget}_${resName}.png ${outputDir}/${widget}.png
  done

  # UI cues
  for icon in ratings_and_ratios servings
  do 
    convert -background none ${icon}.svg -resize ${iconSize}x${iconSize} tmp/${icon}_${resName}.png
    cp tmp/${icon}_${resName}.png ${outputDir}/${icon}.png
  done

  # Graphics on the About screen
  aboutLogoSize=${aboutLogoSizes[sizeIndex]}
  convert -background none dumpling_on_plate.svg -resize ${aboutLogoSize}x${aboutLogoSize} tmp/dumpling_on_plate_${resName}.png
  cp tmp/dumpling_on_plate_${resName}.png ${outputDir}/dumpling_on_plate.png

  # TODO: Sinclair Studios logo. For now we'll leave this hard-wired.

done

rm tmp/*
rmdir tmp
