package ru.yandex.ewmmain.event.repository.customqueryrepo;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.ewmmain.category.repository.CategoryRepository;
import ru.yandex.ewmmain.event.requestdto.filterparam.SearchParam;
import ru.yandex.ewmmain.event.requestdto.filterparam.SortType;
import ru.yandex.ewmmain.event.model.Event;
import ru.yandex.ewmmain.event.model.EventState;
import ru.yandex.ewmmain.user.repository.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class JdbcTemplateRepositoryImpl implements JdbcTemplateRepository {
    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public List<Event> findBySearchParam(SearchParam param) {
        List<String> whereParams = new ArrayList<>();
        List<String> orderParams = new ArrayList<>();


        if (param.getOnlyAvailable() != null && param.getOnlyAvailable()) {
            whereParams.add(" (e.participant_limit = 0 OR" +
                    "  e.participant_limit > (select count(*) from requests as r " +
                    "where r.status like 'CONFIRMED' and r.event_id = e.event_id group by r.event_id) " +
                    "OR (select count(*) from requests as r " +
                    "where r.status like 'CONFIRMED' and r.event_id = e.event_id group by r.event_id) isnull) ");
        }

        if (param.getCategories() != null && !param.getCategories().isEmpty()) {
            List<String> categories = param.getCategories().stream()
                    .map(id -> "e.category_id=" + id)
                    .collect(Collectors.toList());

            whereParams.add("(" + String.join(" or ", categories) + ")");
        }

        if (param.getPaid() != null) {
            whereParams.add("e.paid=" + param.getPaid());
        }

        if (param.getRangeStart() != null) {
            String timestamp = param.getRangeStart().toString().split("T")[0] + " " +
                    param.getRangeStart().toString().split("T")[1];

            whereParams.add("date(e.event_date) > '" + timestamp + "'");
        } else {
            String timestamp = LocalDateTime.now().toString().split("T")[0] + " " +
                    LocalDateTime.now().toString().split("T")[1];

            whereParams.add("date(e.event_date) > '" + timestamp + "'");
        }

        if (param.getRangeEnd() != null) {
            String timestamp = param.getRangeEnd().toString().split("T")[0] + " " +
                    param.getRangeEnd().toString().split("T")[1];
            whereParams.add("date(e.event_date) < '" + timestamp + "'");
        }

        if (param.getUsers() != null && !param.getUsers().isEmpty()) {
            List<String> users = param.getUsers().stream()
                    .map(id -> "e.initiator = " + id)
                    .collect(Collectors.toList());

            whereParams.add("(" + String.join(" or ", users) + ")");

        }

        if (param.getStates() != null && !param.getStates().isEmpty()) {
            param.getStates().stream()
                    .map(state -> "e.state = '" + state.toString() + "'")
                    .forEach(whereParams::add);
        }

        if (param.getText() != null) {
            whereParams.add(" (e.annotation ILIKE '%" + param.getText() + "%' " +
                    "or e.description ilike '%" + param.getText() + "%') ");
        }

        if (param.getSort() != null) {
            orderParams.add(param.getSort().equals(SortType.EVENT_DATE) ? " e.event_date " : " e.event_id "
            );
        }

        String query = "select * from events as e " +
                " where " + String.join(" and ", whereParams) +
                (!orderParams.isEmpty() ? " order by " + String.join(", ", orderParams) : "");


        return jdbcTemplate.query(query, this::mapRow);
    }

    private Event mapRow(ResultSet rs, int rowNum) throws SQLException {
        Event event = new Event();
        event.setId(rs.getLong("event_id"));
        event.setTitle(rs.getString("title"));
        event.setDescription(rs.getString("description"));
        event.setAnnotation(rs.getString("annotation"));
        event.setCreatedOn(rs.getTimestamp("created_on").toLocalDateTime());
        event.setEventDate(rs.getTimestamp("event_date").toLocalDateTime());
        event.setPublishedOn(rs.getTimestamp("published_on") == null ?
                null : rs.getTimestamp("published_on").toLocalDateTime());
        event.setPaid(rs.getBoolean("paid"));
        event.setState(EventState.valueOf(rs.getString("state")));
        event.setRequestModeration(rs.getBoolean("request_moderation"));
        event.setParticipantLimit(rs.getInt("participant_limit"));
        event.setCategory(categoryRepository.findById(rs.getLong("category_id")).get());
        event.setInitiator(userRepository.findById(rs.getLong("initiator")).get());
        event.setLat(rs.getFloat("latitude"));
        event.setLon(rs.getFloat("longitude"));

        return event;
    }


}
