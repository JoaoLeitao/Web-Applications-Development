package isel.leic.daw.dawProject.controller

import com.google.code.siren4j.Siren4J
import com.google.code.siren4j.component.Entity
import com.google.code.siren4j.converter.ReflectingConverter
import io.swagger.annotations.*
import isel.leic.daw.dawProject.configuration.ConflictException
import isel.leic.daw.dawProject.model.entity.Account
import isel.leic.daw.dawProject.model.inputModel.AccountDTO
import isel.leic.daw.dawProject.model.outputModel.account.AccountSirenHTO
import isel.leic.daw.dawProject.service.AccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.ServletRequest

@RestController
@Api(description = "Operations about accounts/users")
@RequestMapping(value = ["/accounts"], produces = [(Siren4J.JSON_MEDIATYPE)])
class AccountRestController {

    @Autowired
    lateinit var accountService: AccountService

    /**
     * Get account with username
     * @param username account username
     * @return account with username passed as parameter
     */
    @ApiOperation(value = "View an account/user", response = AccountDTO::class)
    @ApiResponses(
            ApiResponse(code = 200, message = "Successfully retrieved the account"),
            ApiResponse(code = 400, message = "Bad Request - input maybe not correct"),
            ApiResponse(code = 401, message = "You are not authorized to view this account"),
            ApiResponse(code = 403, message = "Accessing the account you were trying to reach is forbidden"),
            ApiResponse(code = 404, message = "The account you were trying to reach is not found")
    )
    @GetMapping(name = "getAccountById", value = ["/{username}"])
    fun getAccount(
            @ApiParam(value = "Username of the account/user", required = true)
            @PathVariable username: String,
            user: ServletRequest) : ResponseEntity<Entity> {
        val account: Account = accountService.getAccount(username, user)
        val siren = AccountSirenHTO(
                sub = account.sub
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(siren))
    }

    /**
     * Create a new account
     * @param user via request body, has information to create an account
     */
    @ApiOperation(value = "Create an account/user")
    @ApiResponses(
            ApiResponse(code = 200, message = "Successfully retrieved the accounts"),
            ApiResponse(code = 409, message = "Conflict username")
    )
    @PostMapping(name = "postAccount", value = ["/register"])
    fun addAccount(
            @ApiParam(value = "Account/user to be create", required = true)
            user: ServletRequest) : ResponseEntity<Entity> {
        val optionalAccount = accountService.getAccount(user.getAttribute("sub") as String)
        val account : Account
        if(!optionalAccount.isPresent)
            account = accountService.save(AccountDTO(sub = user.getAttribute("sub") as String))
        else account = optionalAccount.get()
        val siren = AccountSirenHTO(
                sub = account.sub
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(siren))
    }

    /**
     * Delete a account
     * @param username account username
     */
    @ApiOperation(value = "Delete an account/user")
    @ApiResponses(
            ApiResponse(code = 200, message = "Successfully deleted the account"),
            ApiResponse(code = 400, message = "Bad Request - input maybe not correct"),
            ApiResponse(code = 401, message = "You are not authorized to delete this account"),
            ApiResponse(code = 403, message = "Deleting the account you were trying to reach is forbidden"),
            ApiResponse(code = 404, message = "The account you were trying to reach is not found")
    )
    @DeleteMapping(name = "deleteAccount", value = ["/username"])
    fun removeAccount(
            @ApiParam(value = "Username of the account/user", required = true)
            @PathVariable username: String,
            user: ServletRequest) {
        accountService.deleteByUsername(username, user)
    }

    /**
     * Update an account
     * @param username account username
     */
    @ApiOperation(value = "Update an account/user")
    @ApiResponses(
            ApiResponse(code = 200, message = "Successfully updated the account"),
            ApiResponse(code = 400, message = "Bad Request - input maybe not correct"),
            ApiResponse(code = 401, message = "You are not authorized to update this account"),
            ApiResponse(code = 403, message = "Updating the account you were trying to reach is forbidden"),
            ApiResponse(code = 404, message = "The account you were trying to reach is not found")
    )
    @PutMapping(name = "editAccount", value = ["{username}"])
    fun updateAccount(
            @ApiParam(value = "Username of the account/user", required = true)
            @PathVariable username: String,
            @ApiParam(value = "Account/user to be create", required = true)
            @RequestBody input : AccountDTO,
            user: ServletRequest) : ResponseEntity<Entity> {
        var account: Account = accountService.getAccount(username, user)

        input.templates = account.templates
        input.checklists = account.checklists

        account = accountService.save(input)
        val siren = AccountSirenHTO(
                sub = account.sub
        )
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(siren))
    }
}