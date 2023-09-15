package com.nttdata.bootcamp.s01accountservice.domain.repository;
import java.util.List;
import org.bson.types.ObjectId;
import com.nttdata.bootcamp.s01accountservice.domain.document.AccountDocument;
import com.nttdata.bootcamp.s01accountservice.model.AccountCreateInput.TypeEnum;
import com.nttdata.bootcamp.s01accountservice.model.AccountDetails;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interfaz que define un AccountMongoRepository para AccountDocument.
 * */
public interface AccountMongoRepository extends ReactiveMongoRepository<AccountDocument, String> {
	/**
	 * AccountMongoRepository.
	 * @param objectId búsqueda por id.
	 * @return un objeto AccountDocument.
	 */
	Mono<AccountDocument> findById(ObjectId objectId);

	/**
	 * AccountMongoRepository.
	 * @param collect busqueda por id.
	 * @return una lista del objeto AccountDocument.
	 */
	Flux<AccountDetails> findByOwnerClientsContains(List<ObjectId> collect);

	/**
	 * AccountMongoRepository.
	 * @param corriente busqueda por id.
	 * @param objectId objectId por id.
	 * @return una lista del objeto AccountDocument.
	 */
	Flux<AccountDocument> findByOwnerClientsContainsAndType(ObjectId objectId, TypeEnum corriente);

	/**
	 * AccountMongoRepository.
	 * @param idList busqueda por id.
	 * @return una lista del objeto AccountDetails.
	 */
	Flux<AccountDetails> findAllById(List<ObjectId> idList);
}
