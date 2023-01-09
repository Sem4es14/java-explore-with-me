package ru.yandex.ewmmain.event.repository.customqueryrepo;

import ru.yandex.ewmmain.event.requestdto.filterparam.SearchParam;
import ru.yandex.ewmmain.event.model.Event;

import java.util.List;

public interface JdbcTemplateRepository {
    List<Event> findBySearchParam(SearchParam param);
}
