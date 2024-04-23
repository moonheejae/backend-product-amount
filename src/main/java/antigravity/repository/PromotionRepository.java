package antigravity.repository;

import antigravity.domain.entity.Promotion;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Repository
public class PromotionRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Promotion> getPromotion(int[] ids) {

        String inClause = IntStream.range(0, ids.length)
                .mapToObj(i -> ":id" + i)
                .collect(Collectors.joining(", "));

        String query = "SELECT * FROM `promotion` WHERE id IN (" + inClause + ")";

        MapSqlParameterSource params = new MapSqlParameterSource();
        for (int i = 0; i < ids.length; i++) {
            params.addValue("id" + i, ids[i]);
        }
        return namedParameterJdbcTemplate.query(
                query,
                params,
                (rs, rowNum) -> Promotion.builder()
                        .id(rs.getInt("id"))
                        .promotion_type(rs.getString("promotion_type"))
                        .name(rs.getString("name"))
                        .discount_type(rs.getString("discount_type"))
                        .discount_value(rs.getInt("discount_value"))
                        .use_started_at(rs.getDate("use_started_at"))
                        .use_ended_at(rs.getDate("use_ended_at"))
                        .build()
        );
    }

}
