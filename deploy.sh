# Get to the Travis build directory, configure git and clone the repo
cd $HOME
git config --global user.email "travis@travis-ci.org"
git config --global user.name "travis-ci"
git clone --quiet --branch=gh-pages https://${GH_TOKEN}@github.com/erchu/bean-cp gh-pages > /dev/null

# Commit and Push the Changes
ls -R
cd gh-pages/upload
cp $HOME/build/erchu/bean-cp/target/beancp-0.1.jar ./upload
git commit -am "Lastest jar on successful travis build $TRAVIS_BUILD_NUMBER auto-pushed to gh-pages"
git push -fq origin gh-pages > /dev/null
