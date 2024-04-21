package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@SpringBootTest
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private PromotionService promotionService;

    @InjectMocks
    private ProductService productService;


    @Test
    void getProductAmount() {
        System.out.println("상품 가격 추출 테스트");
        System.out.println(new Date());

        // 테스트에 필요한 가상 데이터 설정
        Product product = getProduct(1, "상품1", 100);
        ProductInfoRequest request = getProductInfoRequest(1, new int[]{1});


        Promotion promotion = getPromotion(1, "WON", 10, new Date(), new Date());
        when(productRepository.getProduct(1)).thenReturn(product);
        when(promotionService.calculateDiscount(eq(100), any(int[].class))).thenReturn(10);

        ProductAmountResponse response = productService.getProductAmount(request);
        assertNotNull(response);
        assertEquals("상품1", response.getName());
        assertEquals(100, response.getOriginPrice());
        assertEquals(90, response.getDiscountPrice());
        assertEquals(10, response.getFinalPrice());

    }
    private Product getProduct(int id, String name, int price){

        return Product.builder()
                .id(id)
                .name(name)
                .price(price)
                .build();
    }
    private Promotion getPromotion(int id, String type, int value, Date start, Date end){

        return Promotion.builder()
                .id(id)
                .discount_type(type)
                .discount_value(value)
                .use_started_at(start)
                .use_ended_at(end)
                .build();
    }

    private ProductInfoRequest getProductInfoRequest(int product, int[]promotions){

        return ProductInfoRequest.builder()
                .productId(product)
                .couponIds(promotions)
                .build();
    }
}