function togglePublicOptions() {
    var codeAccessCheckbox = document.getElementById('codeAccessCheckbox');
    var publicCheckbox = document.getElementById('publicCheckbox');
    var publicOptions = document.getElementById('publicOptions');
    var codeAccessOptions = document.getElementById('codeAccessOptions');

    if (publicCheckbox.checked) {
        publicOptions.style.display = 'none';
        codeAccessOptions.style.display = 'none';
    } else {
        publicOptions.style.display = 'block';
        if(codeAccessCheckbox.checked){
            codeAccessOptions.style.display = 'block';
        }else{
            codeAccessOptions.style.display = 'none';
        }
    }
}
function modifyTogglePublicOptions() {
    var codeAccessCheckbox = document.getElementById('m-codeAccessCheckbox');
    var publicCheckbox = document.getElementById('m-publicCheckbox');
    var publicOptions = document.getElementById('m-publicOptions');
    var codeAccessOptions = document.getElementById('m-codeAccessOptions');

    if (publicCheckbox.checked) {
        publicOptions.style.display = 'none';
        codeAccessOptions.style.display = 'none';
    } else {
        publicOptions.style.display = 'block';
        if(codeAccessCheckbox.checked){
            codeAccessOptions.style.display = 'block';
        }else{
            codeAccessOptions.style.display = 'none';
        }
    }
}
function modifyTogglePublicOptionsVue(publicChecked,codeAccessChecked) {
    var codeAccessCheckbox = document.getElementById('m-codeAccessCheckbox');
    var publicCheckbox = document.getElementById('m-publicCheckbox');
    var publicOptions = document.getElementById('m-publicOptions');
    var codeAccessOptions = document.getElementById('m-codeAccessOptions');

    if (publicChecked) {
        publicOptions.style.display = 'none';
        codeAccessOptions.style.display = 'none';
    } else {
        publicOptions.style.display = 'block';
        if(codeAccessChecked){
            codeAccessOptions.style.display = 'block';
        }else{
            codeAccessOptions.style.display = 'none';
        }
    }
}
function formatNumberWithCommas(x) {
    let numberString = x.toString();

    return numberString.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}
