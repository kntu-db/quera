package ir.ac.kntu.web.utils.tx;

import ir.ac.kntu.web.utils.connection.ConnectionProvider;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TransactionFilter extends OncePerRequestFilter {

    private final ConnectionProvider provider;

    public TransactionFilter(ConnectionProvider provider) {
        this.provider = provider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        provider.bind();

        filterChain.doFilter(request, response);

        provider.unbind();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getRequestURI().matches(".*/static/.*") || request.getRequestURI().matches(".*/share/.*");
    }
}
