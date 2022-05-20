package src.mapper;

import src.dto.AccountDTO;
import src.model.users.Account;
import src.model.users.AccountType;


public class AccountMapper implements DataMapper<Account, AccountDTO> {

    @Override
    public AccountDTO mapToDto(Account account) {
        AccountDTO dto = new AccountDTO();

        dto.setAccountType(account.getAccountType().name());
        dto.setUsername(account.getUsername());
        dto.setId(account.getId());

        return dto;
    }

    @Override
    public Account mapToEntity(AccountDTO dto) {
        Account account = new Account();

        account.setAccountType(AccountType.valueOf(dto.getAccountType()));
        account.setPassword(dto.getPassword());
        account.setUsername(dto.getUsername());

        return account;
    }
}
