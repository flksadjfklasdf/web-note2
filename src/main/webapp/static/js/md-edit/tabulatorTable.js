var table

var cellContextMenu = [
    {
        label: "新增行",
        action: function(e, cell) {
            var newData = { id: table.getData().length + 1, name: "", col: "", dob: "" };
            table.addData(newData);
        }
    },
    {
        label: "删除行",
        action: function(e, cell) {
            var row = cell.getRow();
            row.delete();
        }
    },
    {
        label: "新增列",
        action: function(e, cell) {
            var columnName = prompt("请输入新列的名称:");
            if (columnName === null || columnName.trim() === "") {
                return;
            }
            var existingColumns = table.getColumns().map(col => col.getField());
            var newField = columnName;
            var counter = 1;

            while (existingColumns.includes(newField)) {
                newField = columnName + '_' + counter;
                counter++;
            }
            var newColumn = { title: columnName, field: newField, editor: true, contextMenu: cellContextMenu, headerSort: false };
            table.addColumn(newColumn);
        }
    },
    {
        label: "删除列",
        action: function(e, cell) {
            var column = cell.getColumn();
            table.deleteColumn(column.getField());
        }
    },
    {
        label: "重命名列",
        action: function(e, cell) {
            var column = cell.getColumn();
            var currentColumnName = column.getField();
            var newColumnName = prompt("请输入新的列名:");
            if (newColumnName === null || newColumnName.trim() === "") {
                return;
            }
            table.updateColumnDefinition(currentColumnName, { title: newColumnName });
        }
    }
];



function exportTableToMarkdown(table) {
    // 获取表格数据
    var data = table.getData();

    // Markdown 表格的头部
    var markdownTable = "| ";
    table.getColumns().forEach(function(column) {
        markdownTable += column.getDefinition().title + " | ";
    });
    markdownTable += "\n";

    // 添加分隔线
    markdownTable += "|";
    table.getColumns().forEach(function(column) {
        markdownTable += " --- |";
    });
    markdownTable += "\n";

    // 添加数据行
    data.forEach(function(row) {
        markdownTable += "| ";
        table.getColumns().forEach(function(column) {
            markdownTable += (row[column.getField()] || "") + " | ";
        });
        markdownTable += "\n";
    });

    markdownTable = markdownTable.slice(0, -1);
    // 输出 Markdown 表格
    //console.log(markdownTable);

    return markdownTable
}

function createTabulatorFromMarkdown(markdownText, targetElement) {


    let lines1 = markdownText.split('\n');

// 过滤出包含 "|" 的行
    let filteredLines = lines1.filter(line => line.includes('|'));
    let resultString = filteredLines.join('\n');
    // 按行分割Markdown文本
    var lines = resultString.trim().split('\n');

    // 如果Markdown表格不符合规范，则返回空
    if (lines.length < 3) {
        console.error('Invalid Markdown table format.');
        return;
    }

    var headerRow = lines[0].trim().split('|').filter(cell => cell.trim() !== '');

    // 解析数据行
    var dataRows = [];
    for (var i = 2; i < lines.length; i++) {
        var rowData = lines[i].trim().split('|')
        rowData.shift()
        rowData.pop()

        if (rowData.length <= headerRow.length) {
            dataRows.push(rowData);
        }
    }

    // 生成Tabulator的列定义
    var columns = headerRow.map((title, index) => ({
        title: title.trim(),
        field: 'field' + index, // 使用默认字段名
        editor:true,
        contextMenu:cellContextMenu,
        headerSort: false
    }));

    // 创建Tabulator实例
    return new Tabulator(targetElement, {
        movableRows: true,
        movableColumns: true,
        layout: "fitColumns",
        columns: columns,
        data: dataRows.map(row => {
            var dataObj = {};
            row.forEach((cell, index) => {
                dataObj['field' + index] = cell.trim();
            });
            return dataObj;
        }),
    });
}