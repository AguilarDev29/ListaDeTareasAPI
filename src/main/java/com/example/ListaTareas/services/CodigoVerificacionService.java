package com.example.ListaTareas.services;
import com.example.ListaTareas.models.codigoVerificacion.CodigoVerificacion;
import com.example.ListaTareas.models.usuario.Usuario;
import com.example.ListaTareas.repositories.CodigoVerificacionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class CodigoVerificacionService {

    private final CodigoVerificacionRepository codigoVerificacionRepository;

    public CodigoVerificacionService(CodigoVerificacionRepository codigoVerificacionRepository) {
        this.codigoVerificacionRepository = codigoVerificacionRepository;
    }

    public Boolean existByCodigo(String codigo){
        return codigoVerificacionRepository.existsByCodigo(codigo);
    }

    public String saveCodigo(Usuario usuario){
        var codigo = codigoVerificacionRepository.save(new CodigoVerificacion(generarCodigo()));
        codigo.setUsuario(usuario);
        codigoVerificacionRepository.save(codigo);
        return codigo.getCodigo();
    }

    private String generarCodigo(){
        String caracteres = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new SecureRandom();
        int longitud = 6;
        StringBuilder sb = new StringBuilder();
        String codigo;
            do {
                for (int i = 0; i < longitud; i++) {
                    int indice = random.nextInt(caracteres.length());
                    sb.append(caracteres.charAt(indice));
                }
                codigo = sb.toString();
            } while (codigoVerificacionRepository.existsByCodigo(codigo));

        return codigo;
    }

    public void deshabilitarCodigo(String codigo){
        codigoVerificacionRepository.findByCodigo(codigo).ifPresent(c -> {
            c.setActivo(false);
            codigoVerificacionRepository.save(c);
        });
    }

    @Scheduled(fixedRate = 60000)
    private void desactivarCodigosVencidos(){
        List<CodigoVerificacion> codigosAVencer = codigoVerificacionRepository
                .findByActivoTrueAndExpiracionBefore(LocalDateTime.now());

        codigosAVencer.forEach(c -> {
            c.setActivo(false);
            codigoVerificacionRepository.save(c);
        });
    }
}
