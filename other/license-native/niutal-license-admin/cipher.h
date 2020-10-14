#ifndef CIPHER_H_INCLUDED
#define CIPHER_H_INCLUDED

#ifdef __cplusplus
extern "C"
{
#endif


void encrypt(char *p, int size);

void decrypt(char *p, int size);

#ifdef __cplusplus
}
#endif
#endif // CIPHER_H_INCLUDED
