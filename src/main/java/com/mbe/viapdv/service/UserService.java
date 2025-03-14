package com.mbe.viapdv.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mbe.viapdv.model.role.Role;
import com.mbe.viapdv.model.user.User;
import com.mbe.viapdv.model.user.dto.CreateUserDTO;
import com.mbe.viapdv.model.user.dto.UpdateUserDTO;
import com.mbe.viapdv.model.userRole.UserRole;
import com.mbe.viapdv.model.userRole.UserRoleId;
import com.mbe.viapdv.model.userRole.dto.ListUserRoleDTO;
import com.mbe.viapdv.repository.IRoleRepository;
import com.mbe.viapdv.repository.IUserRepository;
import com.mbe.viapdv.repository.IUserRoleRepository;
import com.mbe.viapdv.util.ResponseDTO;

@Service
public class UserService {

	@Autowired
	IUserRepository userRepos;

	@Autowired
	IUserRoleRepository userRoleRepos;

	@Autowired
	IRoleRepository roleRepos;

	public ResponseDTO listActiveUsers() {

		List<ListUserRoleDTO> dtoList = userRoleRepos.findByActiveTrue().stream().map((userRole) -> {
			Role role = userRole.getId().getRole();
			User user = userRole.getId().getUser();

			ListUserRoleDTO dtoRow = new ListUserRoleDTO(user.getName(), user.getEmail(), role.getName(), user.getId(),
					role.getId());
			return dtoRow;
		}).toList();

		return new ResponseDTO(HttpStatus.OK.value(), dtoList, null);
	}

	public ResponseDTO getById(long id) {
		Optional<User> userOpt = userRepos.findById(id);

		if (userOpt.isEmpty())
			return new ResponseDTO(HttpStatus.NOT_FOUND.value(), null, "Usuário não encontrado");

		return new ResponseDTO(HttpStatus.OK.value(), userOpt.get(), "Usuário Encontrado");
	}

	public ResponseDTO create(CreateUserDTO data) {

		User user;
		UserRoleId userRoleID;
		UserRole userRole;

		Optional<User> userOpt = userRepos.findByName(data.name());
		if (userOpt.isPresent())
			return new ResponseDTO(HttpStatus.NOT_FOUND.value(), null, "Já existe um Usuário com estes dados");
		else {
			user = new User(data);
			userRepos.save(user);
		}

		// Find Role
		Optional<Role> roleOpt = roleRepos.findById(data.roleId());
		roleOpt.orElseThrow(() -> new RuntimeException("Role não existe"));

		userRoleID = new UserRoleId(user, roleOpt.get());
		userRole = new UserRole(userRoleID);

		try {
			UserRole userRoleSaved = userRoleRepos.save(userRole);
			User userSaved = userRoleSaved.getId().getUser();
			Role roleSaved = userRoleSaved.getId().getRole();

			ListUserRoleDTO userRoleDTO = new ListUserRoleDTO(userSaved.getName(), userSaved.getEmail(),
					roleSaved.getName(), userSaved.getId(), roleSaved.getId());
			return new ResponseDTO(HttpStatus.CREATED.value(), userRoleDTO, "Usuário Criado!");

		} catch (Exception e) {
			System.out.println("Error to Create User=" + e.getMessage());
			throw new RuntimeException("Erro ao Criar Novo Usuário");
		}

	}

	public ResponseDTO update(long id, UpdateUserDTO data) {

		User userToUpdt;

		Optional<User> userOpt = userRepos.findById(id);

		if (userOpt.isEmpty())
			return new ResponseDTO(HttpStatus.NOT_FOUND.value(), null, "Usuário não encontrado");

		userToUpdt = userOpt.get();

		if (data.name() != null && data.name().length() > 0 && data.name() != userToUpdt.getName()) {
			userToUpdt.setName(data.name());
		}

		if (data.email() != null && data.email().length() > 0 && !data.email().equals(userToUpdt.getEmail())) {
			Optional<User> userByEmailOpt = userRepos.findByEmail(data.email());
			if (userByEmailOpt.isPresent())
				return new ResponseDTO(HttpStatus.CONFLICT.value(), null, "Este e-mail já está em uso!");
			
			userToUpdt.setEmail(data.email());
		}

		if (data.roleId() != 0) {
			Optional<Role> roleOpt = roleRepos.findById(data.roleId());

			if (roleOpt.isEmpty()) {
				return new ResponseDTO(HttpStatus.NOT_FOUND.value(), null, "Papel não encontrado!");
			}

			Set<Role> roles = new HashSet<Role>(Arrays.asList(roleOpt.get()));
			userToUpdt.setRoles(roles);
		}

		userToUpdt.setUpdated(LocalDateTime.now());
		userRepos.save(userOpt.get());

		return new ResponseDTO(HttpStatus.OK.value(), userToUpdt, "Usuário Atualizado");
	}

	public ResponseDTO deleteUserById(long id) {
		Optional<User> userOpt = userRepos.findById(id);

		if (userOpt.isEmpty())
			return new ResponseDTO(HttpStatus.NOT_FOUND.value(), null, "Usuário não encontrado");

		userOpt.get().setActive(false);
		userOpt.get().setUpdated(LocalDateTime.now());
		userRepos.save(userOpt.get());

		deleteUserRolesByUser(userOpt.get().getId());

		return new ResponseDTO(HttpStatus.OK.value(), userOpt.get(), "Usuário Removido");
	}

	public ResponseDTO deleteUserRolesByUser(long userId) {
		List<UserRole> userRoleList = userRoleRepos.findByUserId(userId);
		int disabledUserRoleQty = 0;

		if (userRoleList.isEmpty())
			return new ResponseDTO(HttpStatus.NOT_FOUND.value(), null, "Usuário sem Relação com Papel (Função)"); // Alterar
																													// para
																													// log

		for (UserRole userRole : userRoleList) {
			userRole.setActive(false);
			userRole.setUpdated(LocalDateTime.now());

			if (userRoleRepos.save(userRole) != null) {
				disabledUserRoleQty++;
			}
			;
		}

		return new ResponseDTO(HttpStatus.OK.value(), disabledUserRoleQty, "Relações Usuário x Papel Desativadas");
	}
	
	public ResponseDTO getRolesByUser(long userId) {
		
		List<Role> roleList = userRoleRepos.findRolesByUserId(userId);

		if (roleList.isEmpty())
			return new ResponseDTO(HttpStatus.NOT_FOUND.value(), null, "Usuário sem Relação com Papel (Função)");

		return new ResponseDTO(HttpStatus.OK.value(), roleList, "Papéis");
	}
	
	public ResponseDTO addRoleForUser(long userId, long roleId) {
		
		Optional<Role> optRole = roleRepos.findByIdAndActiveTrue(roleId);
		if(optRole.isEmpty())
			return new ResponseDTO(HttpStatus.NOT_FOUND.value(), null, "Esta Função não existe ou não está disponível");

		Optional<User> optUser = userRepos.findById(userId);
		if (optUser.isEmpty())
			return new ResponseDTO(HttpStatus.NOT_FOUND.value(), null, "Usuário não encontrado");
		
		
		List<Role> roleList = userRoleRepos.findRolesByUserId(userId);
		
		boolean existDuplicateRole = roleList.stream().anyMatch(currentRole -> currentRole.getId().equals(roleId));
		if(existDuplicateRole)
			return new ResponseDTO(HttpStatus.CONFLICT.value(), null, "O Usuário já possui esta Função");
		

		// Create UserRole
		User user = optUser.get();
		Role role = optRole.get();
		
		UserRoleId id = new UserRoleId(user, role);
		UserRole userRole = new UserRole(id);
		userRoleRepos.save(userRole);
			
		return new ResponseDTO(HttpStatus.OK.value(), role.getName(), "Papel adicionado ao Usuário");
	}
	
	public ResponseDTO disableRoleForUser(long userId, long roleId) {
		
		Optional<Role> optRole = roleRepos.findByIdAndActiveTrue(roleId);
		if(optRole.isEmpty())
			return new ResponseDTO(HttpStatus.NOT_FOUND.value(), null, "Esta Função não existe ou não está disponível");

		Optional<User> optUser = userRepos.findByIdAndActiveTrue(userId);
		if (optUser.isEmpty())
			return new ResponseDTO(HttpStatus.NOT_FOUND.value(), null, "Usuário não encontrado");
		
		
		Optional<UserRole> userRoleSelected = userRoleRepos.findByUserIdAndRoleId(userId, roleId);
		if(userRoleSelected.isEmpty())
			return new ResponseDTO(HttpStatus.NOT_FOUND.value(), null, "Este Usuário não possui esta Função");
		
		userRoleSelected.get().setActive(false);
		userRoleRepos.save(userRoleSelected.get());
			
		return new ResponseDTO(HttpStatus.OK.value(), null, "Papel removido do Usuário");
	}

}
