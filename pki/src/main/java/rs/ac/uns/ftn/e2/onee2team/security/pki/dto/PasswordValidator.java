package rs.ac.uns.ftn.e2.onee2team.security.pki.dto;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordConstraint, String>{

	@Override
	public void initialize(PasswordConstraint string) {
		
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		
		try  
		{   
		FileInputStream fis=new FileInputStream("common_pass.txt");       
		Scanner sc=new Scanner(fis);    
		while(sc.hasNextLine())  
		{ 
			if(value.toLowerCase().contains(sc.nextLine()))
			{
				sc.close();
				return false;     
			}
		}  
		sc.close();     
		}  
		catch(IOException e)  
		{  
		e.printStackTrace();  
		}  
		
		return true;
	}

}
