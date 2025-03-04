<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="imageModal" :style="s">
    <div id="imageContainer" ref="imageContainer">
        <img :src="src" alt="Image" @click="sclose()" ref="modalImage" @load="adjustImageSize">
    </div>
    <div class="img-ico" @click="sclose">
        <i class="bi bi-x-lg"></i>
    </div>
</div>
<script>
    var imageModal = new Vue({
        el: '#imageModal',
        data: {
            src: '',
            s:{
                display:'none'
            }

        },
        methods: {
            show(x) {
                this.src = x.src;
                this.s.display = 'flex';
                //this.adjustImageSize();
            },
            sclose() {
                this.s.display = 'none';
            },
            adjustImageSize() {
                // Get the container and image elements
                const img = this.$refs.modalImage;

                // Calculate aspect ratios
                const containerRatio = (window.innerWidth-100) / (window.innerHeight-100)
                const imageRatio = img.naturalWidth / img.naturalHeight;

                // Adjust image styles based on aspect ratio comparison
                if (imageRatio > containerRatio) {
                    img.style.width = '100%';
                    img.style.height = 'auto';
                } else {
                    img.style.height = '100%';
                    img.style.width = 'auto';
                }
            },
            handleClick() {
                alert('Element clicked: ' + this.textContent);
            },
            updateClickEvents() {
                const elements = document.querySelectorAll('.custom-img');
                elements.forEach(element => {
                    element.removeEventListener('click', this.ck);
                    element.addEventListener('click', this.ck);
                });
            },
            ck(event) {
                this.show(event.target);
            }
        }
    })
</script>