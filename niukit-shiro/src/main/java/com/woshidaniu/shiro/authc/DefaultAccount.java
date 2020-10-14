/**
 * 
 */
package com.woshidaniu.shiro.authc;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import org.apache.shiro.authc.Account;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.MergableAuthenticationInfo;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 *
 */
public class DefaultAccount implements Account, MergableAuthenticationInfo, Serializable{

	private static final long serialVersionUID = -5599805919092272078L;

	/*--------------------------------------------
    |    I N S T A N C E   V A R I A B L E S    |
    ============================================*/
    /**
     * The authentication information (principals and credentials) for this account.
     */
    private SimpleAuthenticationInfo authcInfo;

    /**
     * The authorization information for this account.
     */
    private SimpleAuthorizationInfo authzInfo;

    /*--------------------------------------------
    |         C O N S T R U C T O R S           |
    ============================================*/

    /**
     * Default no-argument constructor.
     */
    public DefaultAccount() {
    	 this.authcInfo = new SimpleAuthenticationInfo();
         this.authzInfo = new SimpleAuthorizationInfo();
    }

    /**
     * Constructs a SimpleAccount instance for the specified realm with the given principals and credentials.
     *
     * @param principal   the 'primary' identifying attribute of the account, for example, a user id or username.
     * @param credentials the credentials that verify identity for the account
     * @param realmName   the name of the realm that accesses this account data
     */
    public DefaultAccount(Object principal, Object credentials, String realmName) {
        this(principal instanceof PrincipalCollection ? (PrincipalCollection) principal : new SimplePrincipalCollection(principal, realmName), credentials);
    }

    /**
     * Constructs a SimpleAccount instance for the specified realm with the given principals, hashedCredentials and
     * credentials salt used when hashing the credentials.
     *
     * @param principal         the 'primary' identifying attribute of the account, for example, a user id or username.
     * @param hashedCredentials the credentials that verify identity for the account
     * @param credentialsSalt   the salt used when hashing the credentials
     * @param realmName         the name of the realm that accesses this account data
     * @see org.apache.shiro.authc.credential.HashedCredentialsMatcher HashedCredentialsMatcher
     * @since 1.1
     */
    public DefaultAccount(Object principal, Object hashedCredentials, ByteSource credentialsSalt, String realmName) {
        this(principal instanceof PrincipalCollection ? (PrincipalCollection) principal : new SimplePrincipalCollection(principal, realmName),
                hashedCredentials, credentialsSalt);
    }

    /**
     * Constructs a SimpleAccount instance for the specified realm with the given principals and credentials.
     *
     * @param principals  the identifying attributes of the account, at least one of which should be considered the
     *                    account's 'primary' identifying attribute, for example, a user id or username.
     * @param credentials the credentials that verify identity for the account
     * @param realmName   the name of the realm that accesses this account data
     */
    public DefaultAccount(@SuppressWarnings("rawtypes") Collection principals, Object credentials, String realmName) {
        this(new SimplePrincipalCollection(principals, realmName), credentials);
    }

    /**
     * Constructs a SimpleAccount instance for the specified principals and credentials.
     *
     * @param principals  the identifying attributes of the account, at least one of which should be considered the
     *                    account's 'primary' identifying attribute, for example, a user id or username.
     * @param credentials the credentials that verify identity for the account
     */
    public DefaultAccount(PrincipalCollection principals, Object credentials) {
        this.authcInfo = new SimpleAuthenticationInfo(principals, credentials);
        this.authzInfo = new SimpleAuthorizationInfo();
    }

    /**
     * Constructs a SimpleAccount instance for the specified principals and credentials.
     *
     * @param principals        the identifying attributes of the account, at least one of which should be considered the
     *                          account's 'primary' identifying attribute, for example, a user id or username.
     * @param hashedCredentials the hashed credentials that verify identity for the account
     * @param credentialsSalt   the salt used when hashing the credentials
     * @see org.apache.shiro.authc.credential.HashedCredentialsMatcher HashedCredentialsMatcher
     * @since 1.1
     */
    public DefaultAccount(PrincipalCollection principals, Object hashedCredentials, ByteSource credentialsSalt) {
        this.authcInfo = new SimpleAuthenticationInfo(principals, hashedCredentials, credentialsSalt);
        this.authzInfo = new SimpleAuthorizationInfo();
    }

    /**
     * Constructs a SimpleAccount instance for the specified principals and credentials, with the assigned roles.
     *
     * @param principals  the identifying attributes of the account, at least one of which should be considered the
     *                    account's 'primary' identifying attribute, for example, a user id or username.
     * @param credentials the credentials that verify identity for the account
     * @param roles       the names of the roles assigned to this account.
     */
    public DefaultAccount(PrincipalCollection principals, Object credentials, Set<String> roles) {
        this.authcInfo = new SimpleAuthenticationInfo(principals, credentials);
        this.authzInfo = new SimpleAuthorizationInfo(roles);
    }

    /**
     * Constructs a SimpleAccount instance for the specified realm with the given principal and credentials, with the
     * the assigned roles and permissions.
     *
     * @param principal   the 'primary' identifying attributes of the account, for example, a user id or username.
     * @param credentials the credentials that verify identity for the account
     * @param realmName   the name of the realm that accesses this account data
     * @param roleNames   the names of the roles assigned to this account.
     * @param permissions the permissions assigned to this account directly (not those assigned to any of the realms).
     */
    public DefaultAccount(Object principal, Object credentials, String realmName, Set<String> roleNames, Set<Permission> permissions) {
        this.authcInfo = new SimpleAuthenticationInfo(new SimplePrincipalCollection(principal, realmName), credentials);
        this.authzInfo = new SimpleAuthorizationInfo(roleNames);
        this.authzInfo.setObjectPermissions(permissions);
    }

    /**
     * Constructs a SimpleAccount instance for the specified realm with the given principals and credentials, with the
     * the assigned roles and permissions.
     *
     * @param principals  the identifying attributes of the account, at least one of which should be considered the
     *                    account's 'primary' identifying attribute, for example, a user id or username.
     * @param credentials the credentials that verify identity for the account
     * @param realmName   the name of the realm that accesses this account data
     * @param roleNames   the names of the roles assigned to this account.
     * @param permissions the permissions assigned to this account directly (not those assigned to any of the realms).
     */
    public DefaultAccount(@SuppressWarnings("rawtypes") Collection principals, Object credentials, String realmName, Set<String> roleNames, Set<Permission> permissions) {
        this.authcInfo = new SimpleAuthenticationInfo(new SimplePrincipalCollection(principals, realmName), credentials);
        this.authzInfo = new SimpleAuthorizationInfo(roleNames);
        this.authzInfo.setObjectPermissions(permissions);
    }

    /**
     * Constructs a SimpleAccount instance from the given principals and credentials, with the
     * the assigned roles and permissions.
     *
     * @param principals  the identifying attributes of the account, at least one of which should be considered the
     *                    account's 'primary' identifying attribute, for example, a user id or username.
     * @param credentials the credentials that verify identity for the account
     * @param roleNames   the names of the roles assigned to this account.
     * @param permissions the permissions assigned to this account directly (not those assigned to any of the realms).
     */
    public DefaultAccount(PrincipalCollection principals, Object credentials, Set<String> roleNames, Set<Permission> permissions) {
        this.authcInfo = new SimpleAuthenticationInfo(principals, credentials);
        this.authzInfo = new SimpleAuthorizationInfo(roleNames);
        this.authzInfo.setObjectPermissions(permissions);
    }

    /*--------------------------------------------
    |  A C C E S S O R S / M O D I F I E R S    |
    ============================================*/

    /**
     * Returns the principals, aka the identifying attributes (username, user id, first name, last name, etc) of this
     * Account.
     *
     * @return all the principals, aka the identifying attributes, of this Account.
     */
    public PrincipalCollection getPrincipals() {
        return authcInfo.getPrincipals();
    }

    /**
     * Sets the principals, aka the identifying attributes (username, user id, first name, last name, etc) of this
     * Account.
     *
     * @param principals all the principals, aka the identifying attributes, of this Account.
     * @see Account#getPrincipals()
     */
    public void setPrincipals(PrincipalCollection principals) {
        this.authcInfo.setPrincipals(principals);
    }


    /**
     * Simply returns <code>this.authcInfo.getCredentials</code>.  The <code>authcInfo</code> attribute is constructed
     * via the constructors to wrap the input arguments.
     *
     * @return this Account's credentials.
     */
    public Object getCredentials() {
        return authcInfo.getCredentials();
    }

    /**
     * Sets this Account's credentials that verify one or more of the Account's
     * {@link #getPrincipals() principals}, such as a password or private key.
     *
     * @param credentials the credentials associated with this Account that verify one or more of the Account principals.
     * @see org.apache.shiro.authc.Account#getCredentials()
     */
    public void setCredentials(Object credentials) {
        this.authcInfo.setCredentials(credentials);
    }

    /**
     * Returns <code>this.authzInfo.getRoles();</code>
     *
     * @return the Account's assigned roles.
     */
    public Collection<String> getRoles() {
        return authzInfo.getRoles();
    }

    /**
     * Sets the Account's assigned roles.  Simply calls <code>this.authzInfo.setRoles(roles)</code>.
     *
     * @param roles the Account's assigned roles.
     * @see Account#getRoles()
     */
    public void setRoles(Set<String> roles) {
        this.authzInfo.setRoles(roles);
    }

    /**
     * Adds a role to this Account's set of assigned roles.  Simply delegates to
     * <code>this.authzInfo.addRole(role)</code>.
     *
     * @param role a role to assign to this Account.
     */
    public void addRole(String role) {
        this.authzInfo.addRole(role);
    }

    /**
     * Adds one or more roles to this Account's set of assigned roles. Simply delegates to
     * <code>this.authzInfo.addRoles(roles)</code>.
     *
     * @param roles one or more roles to assign to this Account.
     */
    public void addRole(Collection<String> roles) {
        this.authzInfo.addRoles(roles);
    }

    /**
     * Returns all String-based permissions assigned to this Account.  Simply delegates to
     * <code>this.authzInfo.getStringPermissions()</code>.
     *
     * @return all String-based permissions assigned to this Account.
     */
    public Collection<String> getStringPermissions() {
        return authzInfo.getStringPermissions();
    }

    /**
     * Sets the String-based permissions assigned to this Account.  Simply delegates to
     * <code>this.authzInfo.setStringPermissions(permissions)</code>.
     *
     * @param permissions all String-based permissions assigned to this Account.
     * @see org.apache.shiro.authc.Account#getStringPermissions()
     */
    public void setStringPermissions(Set<String> permissions) {
        this.authzInfo.setStringPermissions(permissions);
    }

    /**
     * Assigns a String-based permission directly to this Account (not to any of its realms).
     *
     * @param permission the String-based permission to assign.
     */
    public void addStringPermission(String permission) {
        this.authzInfo.addStringPermission(permission);
    }

    /**
     * Assigns one or more string-based permissions directly to this Account (not to any of its realms).
     *
     * @param permissions one or more String-based permissions to assign.
     */
    public void addStringPermissions(Collection<String> permissions) {
        this.authzInfo.addStringPermissions(permissions);
    }

    /**
     * Returns all object-based permissions assigned directly to this Account (not any of its realms).
     *
     * @return all object-based permissions assigned directly to this Account (not any of its realms).
     */
    public Collection<Permission> getObjectPermissions() {
        return authzInfo.getObjectPermissions();
    }

    /**
     * Sets all object-based permissions assigned directly to this Account (not any of its realms).
     *
     * @param permissions the object-based permissions to assign directly to this Account.
     */
    public void setObjectPermissions(Set<Permission> permissions) {
        this.authzInfo.setObjectPermissions(permissions);
    }

    /**
     * Assigns an object-based permission directly to this Account (not any of its realms).
     *
     * @param permission the object-based permission to assign directly to this Account (not any of its realms).
     */
    public void addObjectPermission(Permission permission) {
        this.authzInfo.addObjectPermission(permission);
    }

    /**
     * Assigns one or more object-based permissions directly to this Account (not any of its realms).
     *
     * @param permissions one or more object-based permissions to assign directly to this Account (not any of its realms).
     */
    public void addObjectPermissions(Collection<Permission> permissions) {
        this.authzInfo.addObjectPermissions(permissions);
    }


    /**
     * Merges the specified <code>AuthenticationInfo</code> into this <code>Account</code>.
     * <p/>
     * If the specified argument is also an instance of {@link SimpleAccount SimpleAccount}, the
     * {@link #isLocked()} and {@link #isCredentialsExpired()} attributes are merged (set on this instance) as well
     * (only if their values are <code>true</code>).
     *
     * @param info the <code>AuthenticationInfo</code> to merge into this account.
     */
    public void merge(AuthenticationInfo info) {
        authcInfo.merge(info);
    }

    /**
     * If the {@link #getPrincipals() principals} are not null, returns <code>principals.hashCode()</code>, otherwise
     * returns 0 (zero).
     *
     * @return <code>principals.hashCode()</code> if they are not null, 0 (zero) otherwise.
     */
    public int hashCode() {
        return (getPrincipals() != null ? getPrincipals().hashCode() : 0);
    }

    /**
     * Returns <code>true</code> if the specified object is also a {@link DefaultAccount DefaultAccount} and its
     * {@link #getPrincipals() principals} are equal to this object's <code>principals</code>, <code>false</code> otherwise.
     *
     * @param o the object to test for equality.
     * @return <code>true</code> if the specified object is also a {@link DefaultAccount DefaultAccount} and its
     *         {@link #getPrincipals() principals} are equal to this object's <code>principals</code>, <code>false</code> otherwise.
     */
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof DefaultAccount) {
        	DefaultAccount sa = (DefaultAccount) o;
            //principal should be unique across the application, so only check this for equality:
            return (getPrincipals() != null ? getPrincipals().equals(sa.getPrincipals()) : sa.getPrincipals() == null);
        }
        return false;
    }

    /**
     * Returns {@link #getPrincipals() principals}.toString() if they are not null, otherwise prints out the string
     * &quot;empty&quot;
     *
     * @return the String representation of this Account object.
     */
    public String toString() {
        return getPrincipals() != null ? getPrincipals().toString() : "empty";
    }

}
