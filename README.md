CmoaQuiz
========

This is a great Android Sample Application for... quiz. You can use and reuse it however you want, set your own question, and enjoy it.

The code is under MIT License so you can do whatever you want but please keep the license and my name with it.

Of course you can contribute to the project by pushing Pull Request if you want to fix/change/add anything.

For the records, this project has been developped in 2012, did a small beta, then had been forgotten. I get it back from the ashes, removed all the spiderwebs and update whatever needed to, removed all the unfinished or uncool code and posted it here.
There is no versionning here before now because there was too much private code in the previous version, sorry for that.

____

#### I just want to **test** this
Nothing easier! Go to the [sample page of the Google Play Store](https://play.google.com/store/apps/details?id=fr.cmoatoto.quiz.general) and try it :)

#### And what if I just want to build it?
Clone this repo, change the files
* quizSample/src/main/res/values/
  * **config.xml** *(change everything you want but you need to fill the `TODO : TO FILL` fields)*
  
Oh, yes, you will need to create a Google Play Game in the Developper console for that. Not the easy part. I will help you on that later (in time, not in the document)...
  
Then a simple `./gradlew clean installDebug` should build and install a fresh version of the Sample

#### But I want to create my own questions!
Cool! Then you have two *(or more)* files to open:
* **quizSample/src/main/res/values/questions.xml** 

contains the questions, 2 false answers, 1 right answer *(in a random order)*, an explanation and a `false` value for the field **`translated`**

* **quizSample/src/main/res/values/questions-{fr,it...}.xml** 

contains the same questions, and a `true` value for the field **`translated`**. You can translate only a few question if you want to.

* **quizSample/src/main/res/xml/questions.xml** 

contains the right answer *(change the `answer` field with the numer of the right answer)* and the name of the submitter of the question.

#### And the code? Where is the code? I want to add unicorns everywhere!
I understand, unicorns are so cool. The whole code is in the `quizBaseLib` folder. I will not explain how everything works *(or maybe I will, but not now)* but it's really easy to create new gamerule, change stuff and add unicorns...

___

Enjoy the vibes, stay true, send me beer by e-mail, take over the world and see you later!

*CmoaToto*
