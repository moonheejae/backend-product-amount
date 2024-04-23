package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository repository;
    private final PromotionService promotionService;

    public boolean isValid( int price ){
        return 10000 <= price && price <= 10000000;
    }

    private void validateRequest(ProductInfoRequest request) {
        Objects.requireNonNull(request, "Request cannot be null.");
        if (request.getProductId() <= 0) {
            throw new IllegalArgumentException("Product ID must be positive.");
        }
        if (request.getCouponIds() == null || request.getCouponIds().length == 0) {
            throw new IllegalArgumentException("Coupon IDs cannot be empty.");
        }
    }
    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {
        validateRequest(request); // request 유효성 검사

        Product product = repository.getProduct(request.getProductId());
        int originProductPrice = product.getPrice();

        if( !isValid(originProductPrice) ){ // 상품 가격 유효성 검사

            throw new IllegalArgumentException("Product Price is not valid.");

        } else{

            int discountedPrice = promotionService.calculateDiscount(originProductPrice, request.getCouponIds());
            int finalPrice = (int) Math.floor( ( (originProductPrice - discountedPrice) / 1000 ) * 1000 );

            ProductAmountResponse response = ProductAmountResponse.builder()
                    .name(product.getName())
                    .originPrice(originProductPrice)
                    .discountPrice(originProductPrice - discountedPrice)
                    .finalPrice(finalPrice)
                    .build();

            return response;
        }
    }
}
