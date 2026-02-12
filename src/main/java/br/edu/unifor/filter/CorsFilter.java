package br.edu.unifor.filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class CorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext,
                      ContainerResponseContext responseContext) throws IOException {
        
        String origin = requestContext.getHeaderString("Origin");
        
        // Log para debug
        System.out.println("🔍 CORS Filter - Origin: " + origin);
        System.out.println("🔍 CORS Filter - Method: " + requestContext.getMethod());
        
        // Permitir apenas origens específicas
        if (origin != null && 
            (origin.equals("http://localhost:4200") || 
             origin.equals("http://localhost:8544"))) {
            
            // Headers CORS obrigatórios
            responseContext.getHeaders().add("Access-Control-Allow-Origin", origin);
            responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
            responseContext.getHeaders().add("Access-Control-Allow-Methods", 
                "GET, POST, PUT, DELETE, OPTIONS, PATCH");
            responseContext.getHeaders().add("Access-Control-Allow-Headers", 
                "Authorization, Content-Type, Accept, Origin, X-Requested-With");
            responseContext.getHeaders().add("Access-Control-Expose-Headers", 
                "Authorization, Content-Disposition");
            responseContext.getHeaders().add("Access-Control-Max-Age", "86400");
            
            System.out.println("✅ CORS Headers adicionados para: " + origin);
        } else {
            System.out.println("❌ Origin não permitido: " + origin);
        }
    }
}