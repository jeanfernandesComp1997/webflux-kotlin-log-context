import http from 'k6/http';
import { check, sleep } from 'k6';
export const options = {
    vus: 2,
    duration: '300s',
};

//export const options = {
//  scenarios: {
//    contacts: {
//      executor: 'ramping-arrival-rate',
//      preAllocatedVUs: 30,
//      timeUnit: '1s',
//      startRate: 10,
//      stages: [
//        { target: 200, duration: '30s' }, // linearly go from 50 iters/s to 200 iters/s for 30s
//        { target: 500, duration: '0' }, // instantly jump to 500 iters/s
//        { target: 500, duration: '5m' }, // continue with 500 iters/s for 10 minutes
//      ],
//    },
//  },
//};
//
//export const options = {
//  scenarios: {
//    constant_request_rate: {
//      executor: 'constant-arrival-rate',
//      rate: 1000,
//      timeUnit: '1s', // 1000 iterations per second, i.e. 1000 RPS
//      duration: '300s',
//      preAllocatedVUs: 100, // how large the initial pool of VUs would be
//      maxVUs: 200, // if the preAllocatedVUs are not enough, we can initialize more
//    },
//  },
//};

export default function () {
let res = http.get('http://webflux-kotlin-log-context.example/characters/7');
check(res, { 'status was 200': (r) => r.status == 200 });
}