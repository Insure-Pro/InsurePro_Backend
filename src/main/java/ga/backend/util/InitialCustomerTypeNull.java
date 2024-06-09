package ga.backend.util;

import ga.backend.customerType.entity.CustomerType;
import ga.backend.customerType.entity.DataType;
import ga.backend.employee.entity.Employee;

public class InitialCustomerTypeNull {
    static public String NULL_NAME = "000";
    static public CustomerType makeCustomerType(Employee employee) {
        CustomerType customerType = new CustomerType();
        customerType.setCompany(employee.getCompany());
        customerType.setEmployeePk(employee.getPk());
        customerType.setName("000");
        customerType.setDetail("customerType을 지정하지 않을 경우 사용하는 default값");
        customerType.setDataType(DataType.ETC);
        customerType.setColor("#000000");
        return customerType;
    }
}
