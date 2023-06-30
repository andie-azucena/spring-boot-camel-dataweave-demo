%dw 2.0
output application/json
---
payload map ((item, index) -> {
  id: item.EMPLOYEE_ID,
  employeeName: item.NAME,
  position: item.POSITION,
  employmentDate: item.DATE_HIRED
})