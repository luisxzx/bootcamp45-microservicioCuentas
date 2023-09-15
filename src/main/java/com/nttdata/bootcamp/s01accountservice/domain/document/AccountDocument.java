package com.nttdata.bootcamp.s01accountservice.domain.document;
import com.nttdata.bootcamp.s01accountservice.model.AccountDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Clase que representa una entidad de AccountDocument.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "accounts")
public class AccountDocument {
    /**
     * Identificador único de la AccountDocument.
     */
    @Id
    private String id;

    /**
     * Identificador único de la type.
     */
    private AccountDetails.TypeEnum type;

    /**
     * Identificador único de la ownerClients.
     */
    private List<String> ownerClients;

    /**
     * Identificador único de la signClients.
     */
    private List<String> signClients;

    /**
     * Identificador único de la balance.
     */
    private BigDecimal balance;

    /**
     * Identificador único de la transactionCount.
     */
    private int transactionCount;

    /**
     * Identificador único de la lastTransactionDate.
     */
    private LocalDate lastTransactionDate;
}
