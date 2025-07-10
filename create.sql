USE lista_tareas_API;

create table usuarios (
	enabled bit,
	id BIGINT UNSIGNED not null auto_increment,
	email varchar(255) not null,
	password varchar(255) not null,
	primary key (id)
) engine=InnoDB;

create table codigos_verificacion (
	activo bit, expiracion datetime(6),
	id BIGINT UNSIGNED not null auto_increment,
	usuario_id BIGINT UNSIGNED, 
	codigo varchar(255),
	primary key (id)
) engine=InnoDB;

create table tareas (
	fecha_creacion datetime(6),
	fecha_finalizacion datetime(6),
	fecha_inicio datetime(6),
	id BIGINT UNSIGNED not null auto_increment,
	usuario_id BIGINT UNSIGNED,
	descripcion varchar(255) not null,
	titulo varchar(255) not null,
	estado enum ('COMPLETADO','EN_PROGRESO','PENDIENTE') not null,
	primary key (id)
) engine=InnoDB;

create table usuario_roles (
	usuario_id BIGINT UNSIGNED not null,
	roles varchar(255)
) engine=InnoDB;



alter table codigos_verificacion add constraint UK5yg29y9666558cmrcp85fqt1d unique (codigo);
alter table codigos_verificacion add constraint FKfss8u32tb0avk5xb7jf8wbhm3
foreign key (usuario_id) references usuarios (id);
alter table tareas add constraint FKdmoaxl7yv4q6vkc9h32wvbddr 
foreign key (usuario_id) references usuarios (id);
alter table usuario_roles add constraint FKuu9tea04xb29m2km5lwe46ua
foreign key (usuario_id) references usuarios (id);
