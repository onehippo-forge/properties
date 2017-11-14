[![Build Status](https://travis-ci.org/onehippo-forge/properties.svg?branch=develop)](https://travis-ci.org/onehippo-forge/properties)

# Properties Component

The properties component allows you to create simple CMS documents having a flat list of name/value pairs. 
It also provides easy locating and using these documents in your HST site frontend.

# Documentation 

Documentation is available at [onehippo-forge.github.io/properties](https://onehippo-forge.github.io/properties)

The documentation is generated by this command from the master branch:

 > mvn clean site:site -Pgithub.pages 
 
The output is in the ```/docs``` directory; push it and GitHub Pages will serve the site automatically. 

For rendering documentation on non-master branches, use the normal site command so the output will be in the ```/target``` 
and therefore ignored by Git.

 > mvn clean site:site

