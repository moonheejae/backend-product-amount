package antigravity.service;

import antigravity.domain.entity.Promotion;
import antigravity.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PromotionService {
    private final PromotionRepository promotionRepository;

    public int calculateDiscount(int originPrice, int[] couponIds) {
        Date currentDate = new Date();
        int totalDiscount = 0;

        List<Promotion> promotionList = promotionRepository.getPromotion(couponIds);

        for (Promotion promotion : promotionList) {
            if ( !isValid(promotion, currentDate) ) {

                throw new IllegalArgumentException("calculateDiscount is not available.");

            } else {

                totalDiscount += calculateDiscount(promotion, originPrice);
            }
        }

        return totalDiscount;
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
