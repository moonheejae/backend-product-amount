package antigravity.service;

import antigravity.domain.entity.Promotion;
import antigravity.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class PromotionService {
    private final PromotionRepository promotionRepository;

    public int calculateDiscount(int originPrice, int[] couponIds) {
        Date currentDate = new Date();

        return Arrays.stream(couponIds)
                .mapToObj(couponId -> promotionRepository.getPromotion(couponId))
                .filter(promotion -> isValid(promotion, currentDate))
                .mapToInt(promotion -> calculateDiscount(promotion, originPrice))
                .sum();
    }
    public int calculateDiscount(Promotion promotion, int originalPrice) {

        String TYPE = promotion.getDiscount_type();
        int VALUE = promotion.getDiscount_value();

        if ("WON".equals(TYPE)) {
            return VALUE;
        } else if ("PERCENT".equals(TYPE)) {
            return originalPrice * VALUE / 100;
        } else {
            return 0;
        }
    }

    public boolean isValid(Promotion promotion, Date currentDate){
        boolean started = promotion.getUse_started_at().before(currentDate);
        boolean ended = promotion.getUse_ended_at().after(currentDate);

        return started && ended;
    }
}
