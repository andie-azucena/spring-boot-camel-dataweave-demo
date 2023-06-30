%dw 2.0
output application/json
---
{
  id: payload.EMPLOYEE_ID,
  employeeName: payload.NAME,
  position: payload.POSITION,
  employmentDate: payload.DATE_HIRED
}