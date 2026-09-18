# OnClinic REST API – Java / Spring Boot

This project maps the User Stories in `User Storie1.docx` to REST endpoints.

## Technology
- Java 17
- Spring Boot 3.4.5
- Spring Web
- Spring Data JPA
- Spring Security + JWT
- H2 database (development)
- Bean Validation

## Run

```bash
mvn spring-boot:run
```

API base URL: `http://localhost:8080/api`

H2 console: `http://localhost:8080/h2-console`
JDBC URL: `jdbc:h2:mem:onclinic`

## Authentication

Register or login:

```http
POST /api/auth/register
Content-Type: application/json

{
  "email": "doctor@example.com",
  "password": "123456",
  "fullName": "Dr. Nguyen",
  "birthday": "1990-01-01",
  "role": "DOCTOR"
}
```

Use the returned JWT:

```http
Authorization: Bearer <token>
```

## User Story → endpoint mapping

### Sprint 1
1. Create Clinic → `POST /api/clinics`
2. Clinic Profile (Doctor) → `GET /api/clinics/mine`
3. Change Clinic Profile → `PUT /api/clinics/mine`
4. Clinic Profile (Patient) → `GET /api/clinics/{id}`
5. Create Examination Slot → `POST /api/slots`
6. View Created Slots → `GET /api/slots/clinic/{clinicId}`
7. Edit Examination Slot → `PUT /api/slots/{id}`
8. Clinic Recommendation → `POST /api/recommendations/clinic`
9. Booking → `POST /api/appointments`

### Sprint 2
10. Upcoming Examination (Doctor) → `GET /api/appointments/upcoming/doctor`
11. Online Examination (Doctor) → `GET /api/appointments/{id}/online-session`
12. Upcoming Examination (Patient) → `GET /api/appointments/upcoming/patient`
13. Online Examination (Patient) → `GET /api/appointments/{id}/online-session`
14. Examination History (Doctor) → `GET /api/appointments/history/doctor`
15. Examination History (Patient) → `GET /api/appointments/history/patient`
16. Create Prescription → `POST /api/prescriptions`
17. View Prescription → `GET /api/prescriptions/mine`

### Sprint 3
18. Profile → `GET /api/users/me`
19. Change Personal Information → `PUT /api/users/me`
20. Contact Us → `POST /api/contact`
21. Feedback for Clinic → `POST /api/feedback/clinic/{clinicId}`
22. View Clinic Feedback → `GET /api/feedback/clinic/{clinicId}`
23. Notification → `GET /api/notifications`
24. Become Partner → `POST /api/users/become-doctor`

### Sprint 4
25. Login → `POST /api/auth/login`
26. Register → `POST /api/auth/register`
27. Forgot Password → `POST /api/auth/forgot-password`
28. Logout → `POST /api/auth/logout`
29. Change Password → `PUT /api/users/me/password`
30. Heartbeat Measure → `POST /api/heartbeat/measure`
31. Online Paying (Patient) → `POST /api/payments/patient`
32. Online Paying (Doctor) → `GET /api/payments/doctor`

## Important production integrations

The source user stories require video calls, heartbeat measurement, email password reset and online payments. This sample exposes REST integration points for them, but a production system should connect:
- WebRTC / Agora / Twilio / another video provider
- Mobile health/device SDK for heartbeat
- SMTP/email provider for password reset
- A real payment gateway with webhook/signature verification
- A real medical/clinical decision-support model if disease prediction is approved and clinically validated

Do not treat the demo recommendation or payment implementation as production medical/payment infrastructure.
