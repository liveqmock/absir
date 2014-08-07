#cd D:/workspace/UI/dev/dwz-ria/bin
MAKE_PATH=$(cd "$(dirname "$0")"; pwd)
cd $MAKE_PATH
echo REM -------------- start package javascript --------------

echo > dwz.ESC.js
cat ../js/dwz.core.js > dwz.ESC.js
cat ../js/dwz.util.date.js >> dwz.ESC.js
cat ../js/dwz.validate.method.js >> dwz.ESC.js
cat ../js/dwz.barDrag.js >> dwz.ESC.js
cat ../js/dwz.drag.js >> dwz.ESC.js
cat ../js/dwz.tree.js >> dwz.ESC.js
cat ../js/dwz.accordion.js >> dwz.ESC.js
cat ../js/dwz.ui.js >> dwz.ESC.js
cat ../js/dwz.theme.js >> dwz.ESC.js
cat ../js/dwz.switchEnv.js >> dwz.ESC.js

cat ../js/dwz.alertMsg.js >> dwz.ESC.js
cat ../js/dwz.contextmenu.js >> dwz.ESC.js
cat ../js/dwz.navTab.js >> dwz.ESC.js
cat ../js/dwz.tab.js >> dwz.ESC.js
cat ../js/dwz.resize.js >> dwz.ESC.js
cat ../js/dwz.dialog.js >> dwz.ESC.js
cat ../js/dwz.dialogDrag.js >> dwz.ESC.js
cat ../js/dwz.sortDrag.js >> dwz.ESC.js
cat ../js/dwz.cssTable.js >> dwz.ESC.js
cat ../js/dwz.stable.js >> dwz.ESC.js
cat ../js/dwz.taskBar.js >> dwz.ESC.js
cat ../js/dwz.ajax.js >> dwz.ESC.js
cat ../js/dwz.pagination.js >> dwz.ESC.js
cat ../js/dwz.database.js >> dwz.ESC.js
cat ../js/dwz.datepicker.js >> dwz.ESC.js
cat ../js/dwz.effects.js >> dwz.ESC.js
cat ../js/dwz.panel.js >> dwz.ESC.js
cat ../js/dwz.checkbox.js >> dwz.ESC.js
cat ../js/dwz.combox.js >> dwz.ESC.js
cat ../js/dwz.history.js >> dwz.ESC.js
cat ../js/dwz.print.js >> dwz.ESC.js

java -jar yuicompressor-2.4.8.jar dwz.ESC.js -o dwz.min.js --nomunge
#rm -rf ./dwz.ESC.js
