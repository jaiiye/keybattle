package ru.anisimov.keybattle.model.user;

import java.time.LocalDate;
import java.util.Arrays;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/31/14
 */
public class UserInfo {
	private long userId;
	private String name;
	private Integer age;
	private Gender gender;
	private LocalDate dateOfBirth;
	private String country;
	private String status;
	private byte[] avatar;
	private boolean hidden;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public byte[] getAvatar() {
		return avatar;
	}

	public void setAvatar(byte[] avatar) {
		this.avatar = avatar;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	private Object[] keyArray() {
		return new Object[]{userId, name, age, gender, dateOfBirth, country, status, avatar, hidden};
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(keyArray());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof UserInfo)) {
			return false;
		}
	
		UserInfo that = (UserInfo) obj;
		return Arrays.deepEquals(this.keyArray(), that.keyArray());
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
				.append("UserInfo: ")
				.append(Arrays.toString(keyArray()))
				.toString();
	}
}
