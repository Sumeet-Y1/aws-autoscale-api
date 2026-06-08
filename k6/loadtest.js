import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate } from 'k6/metrics';

export let errorRate = new Rate('errors');

export let options = {
    stages: [
        { duration: '30s', target: 100 },   // ramp up to 100 users
        { duration: '1m',  target: 500 },   // ramp up to 500 users
        { duration: '1m',  target: 1000 },  // ramp up to 1000 users 
        { duration: '30s', target: 0 },     // ramp down
    ],
    thresholds: {
        http_req_duration: ['p(95)<2000'],  // 95% requests under 2s
        errors: ['rate<0.1'],               // error rate under 10%
    },
};

const BASE_URL = __ENV.ALB_URL;

export default function () {
    let responses = http.batch([
        ['GET', `http://${BASE_URL}/api/hello`],
        ['GET', `http://${BASE_URL}/api/info`],
        ['GET', `http://${BASE_URL}/actuator/health`],
    ]);

    responses.forEach(res => {
        errorRate.add(res.status !== 200);
        check(res, {
            'status is 200': (r) => r.status === 200,
            'response time < 2s': (r) => r.timings.duration < 2000,
        });
    });

    sleep(1);
}