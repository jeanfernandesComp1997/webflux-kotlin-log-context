import http from 'k6/http';
import { check, sleep } from 'k6';
export const options = {
    vus: 2,
    duration: '300s',
};

//export const options = {
//  scenarios: {
//    contacts: {
//      executor: 'ramping-vus',
//      preAllocatedVUs: 10,
//      startVUs: 3,
//      maxVus: 10
//      stages: [
//        { target: 20, duration: '30s' }, // linearly go from 3 VUs to 20 VUs for 30s
//        { target: 100, duration: '0' }, // instantly jump to 100 VUs
//        { target: 100, duration: '10m' }, // continue with 100 VUs for 10 minutes
//      ],
//    },
//  },
//};

export default function () {
let res = http.get('http://webflux-kotlin-log-context.example/characters/suspend/7');
check(res, { 'status was 200': (r) => r.status == 200 });
}