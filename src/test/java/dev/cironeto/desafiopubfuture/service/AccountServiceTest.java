package dev.cironeto.desafiopubfuture.service;

import dev.cironeto.desafiopubfuture.domain.Account;
import dev.cironeto.desafiopubfuture.dto.AccountDto;
import dev.cironeto.desafiopubfuture.repository.AccountRepository;
import dev.cironeto.desafiopubfuture.service.exception.DatabaseException;
import dev.cironeto.desafiopubfuture.service.exception.ResourceNotFoundException;
import dev.cironeto.desafiopubfuture.util.AccountCreator;
import dev.cironeto.desafiopubfuture.util.AccountDtoCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@DisplayName("Tests for Account Service")
class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepositoryMock;

    private Pageable pageable;
    private AccountDto accountDto;
    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;

    @BeforeEach
    void setUp() {
        Account account = AccountCreator.createAccount();
        PageImpl<Account> accountPage = new PageImpl<>(List.of(account));

        accountDto = AccountDtoCreator.createAccountDto();
        existingId = 1L;
        nonExistingId = 99L;
        dependentId = 90L;
        pageable = PageRequest.of(0, 10);

        Mockito.when(accountRepositoryMock.findAll(pageable)).thenReturn(accountPage);

        Mockito.when(accountRepositoryMock.save(ArgumentMatchers.any())).thenReturn(account);

        Mockito.doNothing().when(accountRepositoryMock).deleteById(ArgumentMatchers.anyLong());

        Mockito.when(accountRepositoryMock.getById(ArgumentMatchers.anyLong())).thenReturn(account);

        Mockito.doThrow(EmptyResultDataAccessException.class).when(accountRepositoryMock).deleteById(nonExistingId);

        Mockito.doThrow(DataIntegrityViolationException.class).when(accountRepositoryMock).deleteById(dependentId);
    }

    @Test
    @DisplayName("findAllPaged returns list of all accounts in page object when successful")
    void findAllPaged_ReturnsListOfAllAccountsInsidePage_WhenSuccessful() {
        Page<AccountDto> page = accountService.findAllPaged(pageable);

        Assertions.assertThat(page).isNotEmpty();

        Assertions.assertThat(page.toList()).isNotEmpty().hasSize(1);

        Assertions.assertThat(page.toList().get(0).getId())
                .isEqualTo(AccountCreator.createAccount().getId());
    }

    @Test
    @DisplayName("save persists the account and returns an AccountDto when successful")
    void save_PersistsTheAccountAndReturnsAccountDto_WhenSuccessful() {
        AccountDto accountSaved = accountService.save(accountDto);

        Assertions.assertThat(accountSaved)
                .isNotNull()
                .isEqualTo(AccountDtoCreator.createAccountDto())
                .isInstanceOf(AccountDto.class);
    }

    @Test
    @DisplayName("delete remove account when successful")
    void delete_RemoveAccount_WhenSuccessful() {
        Assertions.assertThatCode(() -> {
                    accountService.delete(existingId);
                })
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete throws ResourceNotFoundException when Id not found")
    void delete_ThrowsResourceNotFoundException_WhenIdNotFound() {
        Assertions.assertThatThrownBy(() -> accountService.delete(nonExistingId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("delete throws DatabaseException when Id has dependent object")
    void delete_ThrowsDatabaseException_WhenIdNotFound() {
        Assertions.assertThatThrownBy(() -> accountService.delete(dependentId))
                .isInstanceOf(DatabaseException.class);
    }

    @Test
    @DisplayName("update replaces an account object with new information passed when successful")
    void update_ReplacesAccountObject_WhenSuccessful() {
        Assertions.assertThatCode(() -> {
                    accountService.update(ArgumentMatchers.anyLong(), accountDto);
                })
                .doesNotThrowAnyException();
    }
}