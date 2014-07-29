cd D:\workspace\UI\dev\dwz-ria\bin

REM -------------- start package javascript --------------

type ..\js\dwz.core.js > dwz.ESC.js
type ..\js\dwz.util.date.js >> dwz.ESC.js
type ..\js\dwz.validate.method.js >> dwz.ESC.js
type ..\js\dwz.barDrag.js >> dwz.ESC.js
type ..\js\dwz.drag.js >> dwz.ESC.js
type ..\js\dwz.tree.js >> dwz.ESC.js
type ..\js\dwz.accordion.js >> dwz.ESC.js
type ..\js\dwz.ui.js >> dwz.ESC.js
type ..\js\dwz.theme.js >> dwz.ESC.js
type ..\js\dwz.switchEnv.js >> dwz.ESC.js

type ..\js\dwz.alertMsg.js >> dwz.ESC.js
type ..\js\dwz.contextmenu.js >> dwz.ESC.js
type ..\js\dwz.navTab.js >> dwz.ESC.js
type ..\js\dwz.tab.js >> dwz.ESC.js
type ..\js\dwz.resize.js >> dwz.ESC.js
type ..\js\dwz.dialog.js >> dwz.ESC.js
type ..\js\dwz.dialogDrag.js >> dwz.ESC.js
type ..\js\dwz.sortDrag.js >> dwz.ESC.js
type ..\js\dwz.cssTable.js >> dwz.ESC.js
type ..\js\dwz.stable.js >> dwz.ESC.js
type ..\js\dwz.taskBar.js >> dwz.ESC.js
type ..\js\dwz.ajax.js >> dwz.ESC.js
type ..\js\dwz.pagination.js >> dwz.ESC.js
type ..\js\dwz.database.js >> dwz.ESC.js
type ..\js\dwz.datepicker.js >> dwz.ESC.js
type ..\js\dwz.effects.js >> dwz.ESC.js
type ..\js\dwz.panel.js >> dwz.ESC.js
type ..\js\dwz.checkbox.js >> dwz.ESC.js
type ..\js\dwz.combox.js >> dwz.ESC.js
type ..\js\dwz.history.js >> dwz.ESC.js
type ..\js\dwz.print.js >> dwz.ESC.js

cscript ESC.wsf -l 1 -ow dwzESC1.js dwz.ESC.js
cscript ESC.wsf -l 2 -ow dwzESC2.js dwzESC1.js
cscript ESC.wsf -l 3 -ow dwzESC3.js dwzESC2.js

type dwzESC2.js > dwz.min.js
#gzip -f dwz.min.js
#copy dwz.min.js.gz dwz.min.gzjs /y

del dwzESC*.js
del dwz.min.js.gz