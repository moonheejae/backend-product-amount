package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository repository;
    private final PromotionService promotionService;

    public boolean isValid( int price ){
        return 10000 <= price && price <= 10000000;
    }
    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {
        Product product = repository.getProduct(request.getProductId());

        int originProductPrice = product.getPrice();

        if(isValid(originProductPrice)){ // 상품 가격 유효셩 검사

            int discountedPrice = promotionService.calculateDiscount(originProductPrice, request.getCouponIds());

            int finalPrice = (int) Math.floor( ( (originProductPrice - discountedPrice) / 1000 ) * 1000 );

            ProductAmountResponse response = ProductAmountResponse.builder()
                    .name(product.getName())
                    .originPrice(originProductPrice)
                    .discountPrice(originProductPrice - discountedPrice)
                    .finalPrice(finalPrice)
                    .build();

            return response;

        } else{
            return null; //todo.예외 처리
        }
    }
}
