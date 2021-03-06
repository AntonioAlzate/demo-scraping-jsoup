package co.com.sofka.scraping.infra.generic;

import co.com.sofka.scraping.domain.generic.DomainEvent;
import co.com.sofka.scraping.domain.generic.EventStoreRepository;
import co.com.sofka.scraping.domain.generic.StoredEvent;
import co.com.sofka.scraping.infra.message.BusService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public abstract class UseCaseHandle {
    @Inject
    private EventStoreRepository repository;

    @Inject
    private BusService busService;

    public void saveProgram(String sectionId, List<DomainEvent> events) {
        events.stream().map(event -> {
            String eventBody = EventSerializer.instance().serialize(event);
            return new StoredEvent(event.getClass().getTypeName(), new Date(), eventBody);
        }).forEach(storedEvent -> repository.saveEvent("section", sectionId, storedEvent));

        events.forEach(busService::send);
    }
}