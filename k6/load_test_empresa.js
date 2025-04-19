import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  // Configuração básica do teste de carga
  stages: [
    { duration: '10s', target: 50 }, // 10 usuários conectados em 30 segundos
    { duration: '30s', target: 500 }, // Clímax de 50 usuários por 1 minuto
    { duration: '30s', target: 0 }, // Reduza de volta a 0
  ],
  thresholds: {
    http_req_duration: ['p(95)<1000'], // 95% das requisições devem ser menores que 1000 ms
  },
};

export default function () {
  const url = 'http://localhost:8080/api/empresas';

  // Realiza a requisição ao endpoint /api/empresas
  const res = http.get(url);

  // Valida que o status code é 200 e que o tempo de resposta é < 1 segundo
  check(res, {
    'status code is 200': (r) => r.status === 200,
    'response time is < 1s': (r) => r.timings.duration < 1000,
  });

  // Aguarda brevemente entre as requisições simulando comportamento real
  sleep(1);
}