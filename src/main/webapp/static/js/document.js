function modifyDocument(id){
    var modal = new bootstrap.Modal(document.getElementById('modifyModal'), {
        keyboard: false
    });
    document.getElementById('modifyModal').setAttribute('data-document-id', id);


    var data={
        collectionId:id
    };

    function action(result) {
        if(result.status === 0){
            modal.show();
            document.getElementById('modifyModal').setAttribute('data-document-id', id);
            document.getElementById('m-name').value=result.data.collectionName;
            document.getElementById('m-description').value=result.data.collectionDescription;
            document.getElementById('m-defaultCheck1').checked = result.data.isPublic;
        }else{
            console.log("ajax错误!"+result)
        }
    }

    doPost(data,baseUrl+'/collection/getCollectionById',action)

}


function doModifyDocument(){

    var id = document.getElementById('modifyModal').getAttribute('data-document-id');

    var data={
        collectionId:id,
        collectionName:document.getElementById('m-name').value,
        collectionDescription:document.getElementById('m-description').value,
        isPublic:document.getElementById('m-defaultCheck1').checked
    };

    function action(result) {
        if(result.status === 0){
            var modal = bootstrap.Modal.getInstance(document.getElementById('modifyModal'))
            modal.hide()
            location.reload()
        }else{
            console.log("ajax错误!"+result)
        }
    }

    doPost(data,baseUrl+'/collection/modify',action)

}



function createNewDocument(){
    var modal = new bootstrap.Modal(document.getElementById('createModal'), {
        keyboard: false
    });

    modal.show();
}

function deleteDocument(id,name){
    var modal = new bootstrap.Modal(document.getElementById('deleteModal'), {
        keyboard: false
    });

    // 更新弹窗内容
    document.getElementById('deleteModalBody').innerText = '确定要删除文档 ' + name + ' 吗？';

    // 显示弹窗
    modal.show();

    // 存储当前要删除的图片 id，以备删除时使用
    document.getElementById('deleteModal').setAttribute('data-document-id', id);
}
function doDeleteDocument() {
    // 获取要删除的图片信息
    var id = document.getElementById('deleteModal').getAttribute('data-document-id');

    // 执行删除操作的逻辑
    // 可以在这里添加删除图片的代码
    console.log('删除文档 ' + id);

    // 隐藏弹窗
    var modal = bootstrap.Modal.getInstance(document.getElementById('deleteModal'));
    modal.hide();
}


function removeDocument(id){
    var modal = new bootstrap.Modal(document.getElementById('removeModal'), {
        keyboard: false
    });

    document.getElementById('removeModalBody').innerText = '确定要取消收藏文档 ' + id + ' 吗？';

    modal.show();

    document.getElementById('removeModal').setAttribute('data-document-id', id);
}
function doRemoveDocument() {

    var id = document.getElementById('removeModal').getAttribute('data-document-id');

    console.log('删除文档 ' + id);

    var modal = bootstrap.Modal.getInstance(document.getElementById('removeModal'));

    modal.hide();
}


function doPost(data,url,action){
    $.ajax({
        url: url,
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function(response) {
            action(response)
        },
        error: function(error) {
            action(error)
        }
    });
}