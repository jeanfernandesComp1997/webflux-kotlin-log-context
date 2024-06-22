import http from 'k6/http';
import { check, sleep } from 'k6';
export const options = {
vus: 2,
duration: '300s',
};

export default function () {
let res = http.get('http://webflux-kotlin-log-context.example/characters/7');
check(res, { 'status was 200': (r) => r.status == 200 });
}