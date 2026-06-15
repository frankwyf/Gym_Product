import { useEffect, useMemo, useState } from 'react'
import { Alert, Pressable, StyleSheet, Text, TextInput, View } from 'react-native'
import { PrimaryButton } from '../../components/PrimaryButton'
import { Screen } from '../../components/Screen'
import { SectionCard } from '../../components/SectionCard'
import { colors, spacing } from '../../constants/theme'
import { useI18n } from '../../hooks/useI18n'
import type { ShippingAddress } from '../../types/models'
import { getStoredAddresses, setStoredAddresses } from '../../utils/storage'

const emptyForm = {
  id: 0,
  linkMan: '',
  mobile: '',
  address: '',
  code: '',
  isDefault: false
}

export function AddressesScreen() {
  const { t } = useI18n()
  const [addresses, setAddresses] = useState<ShippingAddress[]>([])
  const [form, setForm] = useState<ShippingAddress>(emptyForm)

  useEffect(() => {
    getStoredAddresses().then((items) => setAddresses(items))
  }, [])

  const nextId = useMemo(() => {
    if (addresses.length === 0) {
      return 1
    }
    return Math.max(...addresses.map((item) => item.id)) + 1
  }, [addresses])

  const persist = async (next: ShippingAddress[]) => {
    setAddresses(next)
    await setStoredAddresses(next)
  }

  const saveAddress = async () => {
    if (!form.linkMan.trim() || !form.mobile.trim() || !form.address.trim()) {
      Alert.alert(t('auth.tips'), t('addresses.required'))
      return
    }

    let nextList: ShippingAddress[]
    if (form.id) {
      nextList = addresses.map((item) => (item.id === form.id ? form : item))
    } else {
      nextList = [...addresses, { ...form, id: nextId }]
    }

    if (form.isDefault) {
      nextList = nextList.map((item) => ({ ...item, isDefault: item.id === (form.id || nextId) }))
    }

    await persist(nextList)
    setForm(emptyForm)
  }

  const editAddress = (item: ShippingAddress) => {
    setForm(item)
  }

  const removeAddress = async (id: number) => {
    const next = addresses.filter((item) => item.id !== id)
    await persist(next)
  }

  const setDefault = async (id: number) => {
    const next = addresses.map((item) => ({ ...item, isDefault: item.id === id }))
    await persist(next)
  }

  return (
    <Screen>
      <SectionCard title={t('addresses.title')} subtitle={t('addresses.subtitle')}>
        <TextInput style={styles.input} placeholder={t('addresses.name')} placeholderTextColor={colors.textMuted} value={form.linkMan} onChangeText={(v) => setForm({ ...form, linkMan: v })} />
        <TextInput style={styles.input} placeholder={t('addresses.mobile')} placeholderTextColor={colors.textMuted} value={form.mobile} onChangeText={(v) => setForm({ ...form, mobile: v })} keyboardType="phone-pad" />
        <TextInput style={styles.input} placeholder={t('addresses.street')} placeholderTextColor={colors.textMuted} value={form.address} onChangeText={(v) => setForm({ ...form, address: v })} />
        <TextInput style={styles.input} placeholder={t('addresses.postal')} placeholderTextColor={colors.textMuted} value={form.code ?? ''} onChangeText={(v) => setForm({ ...form, code: v })} />

        <View style={styles.formActions}>
          <PrimaryButton title={form.id ? t('addresses.update') : t('addresses.add')} onPress={() => void saveAddress()} />
          {form.id ? <PrimaryButton title={t('addresses.cancelEdit')} secondary onPress={() => setForm(emptyForm)} /> : null}
        </View>
      </SectionCard>

      <SectionCard title={t('addresses.list')} subtitle={`Saved ${addresses.length} address(es)`}>
        {addresses.map((item) => (
          <View key={item.id} style={styles.card}>
            <Text style={styles.name}>{`${item.linkMan} · ${item.mobile}`}</Text>
            <Text style={styles.addr}>{item.address}</Text>
            {item.code ? <Text style={styles.addr}>{`${t('addresses.code')}: ${item.code}`}</Text> : null}
            <View style={styles.tagRow}>
              {item.isDefault ? <Text style={styles.defaultTag}>{t('addresses.default')}</Text> : null}
            </View>
            <View style={styles.actions}>
              <Pressable onPress={() => editAddress(item)}><Text style={styles.actionText}>{t('addresses.edit')}</Text></Pressable>
              <Pressable onPress={() => void setDefault(item.id)}><Text style={styles.actionText}>{t('addresses.setDefault')}</Text></Pressable>
              <Pressable onPress={() => void removeAddress(item.id)}><Text style={[styles.actionText, styles.dangerText]}>{t('addresses.delete')}</Text></Pressable>
            </View>
          </View>
        ))}
      </SectionCard>
    </Screen>
  )
}

const styles = StyleSheet.create({
  input: {
    backgroundColor: colors.surfaceAlt,
    color: colors.text,
    borderRadius: 14,
    paddingHorizontal: spacing.md,
    paddingVertical: spacing.sm,
    borderWidth: 1,
    borderColor: colors.border
  },
  formActions: {
    flexDirection: 'row',
    gap: spacing.sm,
    flexWrap: 'wrap'
  },
  card: {
    backgroundColor: colors.surfaceAlt,
    borderRadius: 16,
    padding: spacing.md,
    gap: spacing.xs
  },
  name: {
    color: colors.text,
    fontWeight: '700',
    fontSize: 15
  },
  addr: {
    color: colors.textMuted,
    lineHeight: 20
  },
  tagRow: {
    flexDirection: 'row',
    gap: spacing.xs
  },
  defaultTag: {
    color: colors.success,
    fontWeight: '700'
  },
  actions: {
    flexDirection: 'row',
    gap: spacing.md
  },
  actionText: {
    color: colors.accent,
    fontWeight: '700'
  },
  dangerText: {
    color: colors.danger
  }
})
