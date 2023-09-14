package com.nttdata.bootcamp.s01accountservice.common;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import com.nttdata.bootcamp.s01accountservice.domain.document.AccountDocument;
import com.nttdata.bootcamp.s01accountservice.model.AccountCreateInput;
import com.nttdata.bootcamp.s01accountservice.model.AccountDetails;
public class AccountMapper {

    /**
     * Método para AccountMapper.
     * @param accountDetails parametro de AccountDocument.
     * @return AccountDocument.
     */

    public static AccountDocument mapDtoToDocument(final AccountDetails accountDetails) {
        return AccountDocument.builder()
                .id(accountDetails.getId())
                .type(accountDetails.getType())
                .ownerClients(accountDetails.getOwnerClients())
                .signClients(accountDetails.getSignClients())
                .balance(accountDetails.getBalance())
                .transactionCount(accountDetails.getTransactionCount())
                .lastTransactionDate(accountDetails.getLastTransactionDate() != null ? accountDetails.getLastTransactionDate().toLocalDate() : null)
                .build();
    }

    /**
     * Método para AccountMapper.
     * @param accountDocument parametro de AccountDetails.
     * @return AccountDocument.
     */
    public static AccountDetails mapDocumentToDto(final AccountDocument accountDocument) {
        AccountDetails accountDetails = new AccountDetails();
        accountDetails.setId(accountDocument.getId());
        accountDetails.setType(accountDocument.getType());
        accountDetails.setOwnerClients(accountDocument.getOwnerClients());
        accountDetails.setSignClients(accountDocument.getSignClients());
        accountDetails.setBalance(accountDocument.getBalance());
        accountDetails.setTransactionCount(accountDocument.getTransactionCount());
        accountDetails.setLastTransactionDate(accountDocument.getLastTransactionDate() != null ? accountDocument.getLastTransactionDate().atStartOfDay().atOffset(OffsetDateTime.now().getOffset()) : null);
        return accountDetails;
    }

    /**
     * Método para AccountMapper.
     * @param accountCreateInput parametro de AccountDetails.
     * @return AccountDocument.
     */
    public static AccountDocument mapCreateInputToDocument(final AccountCreateInput accountCreateInput) {
        return AccountDocument.builder()
                .type(mapAccountCreateInputTypeEnumToAccountDetailsTypeEnum(accountCreateInput.getType()))
                .ownerClients(accountCreateInput.getOwnerClients())
                .signClients(accountCreateInput.getSignClients())
                .balance(BigDecimal.ZERO)
                .transactionCount(0)
                .lastTransactionDate(LocalDate.now())
                .build();
    }

    /**
     * Método para AccountMapper.
     * @param accountDetailsTypeEnum parametro de AccountCreateInput.TypeEnum.
     * @return AHORRO, CORRIENTE, PLAZOFIJO.
     */
    public static AccountCreateInput.TypeEnum mapAccountDetailsTypeEnumToAccountCreateInputTypeEnum(final AccountDetails.TypeEnum accountDetailsTypeEnum) {
        switch (accountDetailsTypeEnum) {
            case AHORRO:
                return AccountCreateInput.TypeEnum.AHORRO;
            case CORRIENTE:
                return AccountCreateInput.TypeEnum.CORRIENTE;
            case PLAZOFIJO:
                return AccountCreateInput.TypeEnum.PLAZOFIJO;
            default:
                throw new IllegalArgumentException("Unsupported mapping for AccountDetails.TypeEnum value: " + accountDetailsTypeEnum);
        }
    }

    /**
     * Método para AccountMapper.
     * @param accountCreateInputTypeEnum parametro de AccountCreateInput.TypeEnum.
     * @return AHORRO, CORRIENTE, PLAZOFIJO.
     */
    public static AccountDetails.TypeEnum mapAccountCreateInputTypeEnumToAccountDetailsTypeEnum(final AccountCreateInput.TypeEnum accountCreateInputTypeEnum) {
        switch (accountCreateInputTypeEnum) {
            case AHORRO:
                return AccountDetails.TypeEnum.AHORRO;
            case CORRIENTE:
                return AccountDetails.TypeEnum.CORRIENTE;
            case PLAZOFIJO:
                return AccountDetails.TypeEnum.PLAZOFIJO;
            default:
                throw new IllegalArgumentException("Unsupported mapping for AccountCreateInput.TypeEnum value: " + accountCreateInputTypeEnum);
        }
    }
}
