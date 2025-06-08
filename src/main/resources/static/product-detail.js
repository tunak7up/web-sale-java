document.addEventListener('DOMContentLoaded', () => {
    // Logic cho Product Gallery (chuyển đổi ảnh thumbnail)
    const mainImage = document.getElementById('main-image');
    const thumbnails = document.querySelectorAll('.thumbnail-images .thumbnail img');

    if (mainImage && thumbnails.length > 0) {
        thumbnails.forEach(thumbnail => {
            thumbnail.addEventListener('click', () => {
                // Xóa 'active' khỏi tất cả thumbnails
                thumbnails.forEach(t => t.parentElement.classList.remove('active'));
                // Đặt 'active' cho thumbnail được click
                thumbnail.parentElement.classList.add('active');
                // Thay đổi ảnh chính
                mainImage.src = thumbnail.src;
            });
        });
    }

    // Logic cho Product Tabs (Mô tả, Thông số kỹ thuật, Đánh giá)
    const tabHeaders = document.querySelectorAll('.tabs-header .tab-header');
    const tabContents = document.querySelectorAll('.tabs-content .tab-content');

    if (tabHeaders.length > 0 && tabContents.length > 0) {
        tabHeaders.forEach(header => {
            header.addEventListener('click', () => {
                const targetTab = header.dataset.tab;

                // Xóa 'active' khỏi tất cả tab headers và contents
                tabHeaders.forEach(h => h.classList.remove('active'));
                tabContents.forEach(content => content.classList.remove('active'));

                // Thêm 'active' vào tab header được click
                header.classList.add('active');
                // Hiển thị tab content tương ứng
                document.getElementById(targetTab).classList.add('active');
            });
        });
    }

    // Logic cho Quantity Selector (tăng giảm số lượng)
    const quantitySelector = document.querySelector('.quantity-selector');
    if (quantitySelector) {
        const minusBtn = quantitySelector.querySelector('.minus');
        const plusBtn = quantitySelector.querySelector('.plus');
        const quantityInput = quantitySelector.querySelector('input[type="number"]');

        if (minusBtn && plusBtn && quantityInput) {
            minusBtn.addEventListener('click', () => {
                let currentValue = parseInt(quantityInput.value);
                if (currentValue > parseInt(quantityInput.min)) {
                    quantityInput.value = currentValue - 1;
                }
            });

            plusBtn.addEventListener('click', () => {
                let currentValue = parseInt(quantityInput.value);
                let maxValue = parseInt(quantityInput.max);
                if (currentValue < maxValue) {
                    quantityInput.value = currentValue + 1;
                }
            });

            // Đảm bảo giá trị không vượt quá min/max khi nhập tay
            quantityInput.addEventListener('change', () => {
                let value = parseInt(quantityInput.value);
                let min = parseInt(quantityInput.min);
                let max = parseInt(quantityInput.max);

                if (isNaN(value) || value < min) {
                    quantityInput.value = min;
                } else if (value > max) {
                    quantityInput.value = max;
                }
            });
        }
    }
});