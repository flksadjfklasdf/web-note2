<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="window-toast" class="toast toast-container position-fixed top-0 start-50 translate-middle-x p-1"
     data-bs-delay="1000" role="alert" aria-live="assertive" aria-atomic="true">
    <div class="d-flex">
        <div class="toast-body">
            {{ message }}
        </div>
        <button type="button" class="btn-close me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
    </div>
</div>
<script>
    var windowToastManager = new Vue({
        el: '#window-toast',
        data: {
            message: '',
        },
        methods: {
            showToast(type, message) {
                var elementById = document.getElementById('window-toast');
                if (type === 'success') {
                    elementById.style.backgroundColor = '#AAFFAA';
                } else if (type === 'failed') {
                    elementById.style.backgroundColor = '#FFAAAA';
                }
                this.message = message;

                var toastElement = new bootstrap.Toast(document.querySelector('.toast'));
                toastElement.show();
            },
        }
    })
</script>