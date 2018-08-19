package isel.leic.daw.dawProject.service

import isel.leic.daw.dawProject.configuration.ForbiddenException
import isel.leic.daw.dawProject.configuration.NotFoundException
import isel.leic.daw.dawProject.model.inputModel.AccountDTO
import isel.leic.daw.dawProject.model.entity.Account
import isel.leic.daw.dawProject.repository.AccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import javax.servlet.ServletRequest

@Service
class AccountService {

    @Autowired
    lateinit var accountRepository: AccountRepository

    fun getAccount(sub: String) : Optional<Account> {
        return accountRepository.findBySub(sub)
    }

    fun getAccount(sub: String, user: ServletRequest) : Account {
        val acc = accountRepository
                .findBySub(sub)
        return if(sub == user.getAttribute("sub"))
            acc
                    .orElseThrow { NotFoundException("Username $sub not found") }
        else throw ForbiddenException(
                "Username $sub is forbidden to the current user " + user.getAttribute("username"))
    }

    fun deleteByUsername(username: String, user: ServletRequest) {
        accountRepository.deleteById(getAccount(username, user).sub)
    }

    fun save(dto: AccountDTO) : Account {
        return accountRepository.saveAndFlush(
                Account(
                        checklists = dto.checklists,
                        templates = dto.templates,
                        sub = dto.sub
                )
        )
    }
}