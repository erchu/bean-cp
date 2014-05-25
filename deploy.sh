# Get to the Travis build directory, configure git and clone the repo

# Build javadoc

cp $HOME/build/erchu/bean-cp
mvn javadoc:javadoc
mvn javadoc:jar

cd $HOME
git config --global user.email "travis@travis-ci.org"
git config --global user.name "travis-ci"
git clone --quiet --branch=gh-pages https://${GH_TOKEN}@github.com/erchu/bean-cp gh-pages > /dev/null

# Prepare files
cd $HOME/gh-pages/upload
git rm -rf ./javadoc
cp $HOME/build/erchu/bean-cp/target/beancp-*.jar ./upload
cp $HOME/build/erchu/bean-cp/target/apidocs ./apidocs

# Commit and Push the Changes
git add .
git commit -m "Lastest jar on successful travis build $TRAVIS_BUILD_NUMBER auto-pushed to gh-pages"
git push -fq origin gh-pages > /dev/null
