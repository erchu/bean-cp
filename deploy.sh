# Build javadoc

cd $HOME/build/erchu/bean-cp
mvn javadoc:javadoc
mvn javadoc:jar

# Configure git and clone the repo

cd $HOME
git config --global user.email "travis@travis-ci.org"
git config --global user.name "travis-ci"
git clone --quiet --branch=gh-pages https://${GH_TOKEN}@github.com/erchu/bean-cp gh-pages > /dev/null

# Copy and prepare files to push

cd $HOME/gh-pages
rm -rf ./apidocs/development/*
cp $HOME/build/erchu/bean-cp/target/beancp-*.jar ./download/development
cp -R $HOME/build/erchu/bean-cp/target/apidocs/* ./apidocs/development

# Commit and push the changes

git add . --all
git commit -m "Travis build $TRAVIS_BUILD_NUMBER: auto-pushed to gh-pages"
git push -fq origin gh-pages > /dev/null
