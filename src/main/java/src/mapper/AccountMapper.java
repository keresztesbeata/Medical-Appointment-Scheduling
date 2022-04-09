package src.mapper;

import src.dto.AccountDTO;
import src.model.users.Account;

public class AccountMapper implements DataMapper<Account, AccountDTO> {
    @Override
    public AccountDTO mapToDto(Account entity) {
        AccountDTO dto = new AccountDTO();

        // todo set fields

        return dto;
    }

    @Override
    public Account mapToEntity(AccountDTO dto) {
        Account account = new Account();

        // todo set fields

        return account;
    }
}
